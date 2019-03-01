package cn.tujia.swiftutil;

import cn.tujia.swiftutil.common.Constants;
import cn.tujia.swiftutil.config.SwiftServiceProperties;
import cn.tujia.swiftutil.enums.SwiftAccountEnum;
import cn.tujia.swiftutil.model.ContainerStoragePolicy;
import cn.tujia.swiftutil.model.SwiftObject;
import cn.tujia.swiftutil.model.TokenInfo;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.codehaus.jackson.map.ObjectMapper;
import org.javaswift.joss.client.factory.AccountConfig;
import org.javaswift.joss.client.factory.AccountFactory;
import org.javaswift.joss.client.factory.AuthenticationMethod;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.model.StoredObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static cn.tujia.swiftutil.enums.SwiftAccountEnum.CERTIFICATIONIMAGE;
import static cn.tujia.swiftutil.enums.SwiftAccountEnum.FILEPRIVATE;
import static cn.tujia.swiftutil.enums.SwiftAccountEnum.getAccountByAccountName;


/**
 * main API
 *
 * @author lk
 * @date 2019/2/26
 */
@Component
public class SwiftService {

  private final static Logger logger = LoggerFactory.getLogger(SwiftService.class);

  /**
   * 敏感Account
   */
  private final static Set<String> PRIVATE_ACCOUNTS =
      new HashSet<>(Arrays.asList(FILEPRIVATE.getAccountName(), CERTIFICATIONIMAGE.getAccountName()));

  private String name;
  private String password;
  private String authUrl;
  private Auth auth;
  private String env;
  private String uploadUrl;
  private String downloadUrl;
  private String apiKey;
  private String secretKey;

  public SwiftService(SwiftServiceProperties swiftProps, String env) {
    this.name = swiftProps.getName();
    this.password = swiftProps.getPassword();
    this.authUrl = swiftProps.getAuthUrl();
    this.auth = Auth.create(swiftProps.getQnAccessKey(), swiftProps.getQnSecretKey());
    this.env = env;
    this.uploadUrl = swiftProps.getUploadUrl();
    this.downloadUrl = swiftProps.getDownloadUrl();
    this.apiKey = swiftProps.getApiKey();
    this.secretKey = swiftProps.getSecretKey();
  }


  /**
   * 获取文件url 兼容qiniu和swift
   *
   * @param swiftUrl
   * @param fileKey
   * @param bucket
   * @param suffix
   * @param expire
   * @return
   */
  public String getUrl(String swiftUrl, String fileKey, String bucket, String suffix, Long expire) {
    if (StringUtils.isEmpty(swiftUrl)) {
      return getUrlByQiniu(fileKey, bucket, suffix, expire);
    }
    return getUrlBySwift(swiftUrl, suffix);
  }

  /**
   * @param accountName
   * @param objectName  保证唯一
   * @param inputStream
   * @return
   */
  public String uploadBySwift(String accountName, String objectName, InputStream inputStream) {
    Container container = containerInit(accountName);
    if (container == null) {
      logger.error("error account name:{}", accountName);
      return null;
    }
    StoredObject storedObject = container.getObject(objectName);
    storedObject.uploadObject(inputStream);
    return "/" + accountName + "/" + container.getName() + "/" + objectName;
  }

  public String uploadByQiniu(String bucket, String fileName, InputStream inputStream) {
    Configuration cfg = new Configuration(Zone.zone0());
    UploadManager uploadManager = new UploadManager(cfg);

    String upToken = auth.uploadToken(bucket);
    try {
      com.qiniu.http.Response response = uploadManager.put(inputStream, fileName, upToken, null, null);
      DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
      logger.info("upload success fileKey is - " + putRet.key);
      return putRet.key;
    } catch (QiniuException e) {
      logger.warn("upload failed fileKey: {}, error: {}", fileName, e);
    }
    return null;
  }

  /**
   * 上传文件
   * 兼容qiniu和swift
   *
   * @param accountName
   * @param inputStream
   * @param suffix
   * @return
   */
  public String upload(String accountName, String bucket, String fileName, InputStream inputStream, String suffix) {
    if (StringUtils.isEmpty(accountName) && StringUtils.isEmpty(bucket)) {
      return null;
    }
    if (!StringUtils.isEmpty(accountName)) {
      String objectName = System.currentTimeMillis() + "_" + (Math.random() * 9 + 1) * 100000 + suffix;
      return uploadBySwift(accountName, objectName, inputStream);
    }
    return uploadByQiniu(bucket, fileName, inputStream);
  }

  /**
   * 获取swift文件
   *
   * @param swiftUrl
   * @param suffix
   * @return
   */
  public String getUrlBySwift(String swiftUrl, String suffix) {
    suffix = formatSuffix(suffix);

    SwiftObject swiftObject = new SwiftObject(swiftUrl, suffix);

    if (PRIVATE_ACCOUNTS.contains(swiftObject.getAccount())) {
      if (StringUtils.isEmpty(suffix)) {
        return getPrivateUrlBySwift(swiftUrl, 3600L);
      }
      return getPrivateImageBySwift(swiftObject);
    }
    return Util.combainUrlPart(downloadUrl, swiftObject.getSuffixUrl());
  }

  private String getPrivateImageBySwift(SwiftObject swiftObject) {
    TokenInfo tokenInfo = getToken(swiftObject.getAccount());

    return Util.combainUrlPart(downloadUrl, swiftObject.getSuffixUrl()) + "?token=" + tokenInfo.getToken();

  }

  /**
   * 获取私密文件
   *
   * @param swiftUrl
   * @param expire
   * @return
   */
  public String getPrivateUrlBySwift(String swiftUrl, Long expire) {


    SwiftObject swiftObject = new SwiftObject(swiftUrl);

    Account account = accountInit(swiftObject.getAccount());
    Container container = account.getContainer(swiftObject.getContainer());
    if (!container.exists()) {
      logger.error("error container name:{}", swiftObject.getContainer());
      return null;
    }
    container.makePublic();
    StoredObject storedObject = container.getObject(swiftObject.getSwiftKey());
    return storedObject.getTempGetUrl(expire);
  }

  /**
   * 获取七牛文件
   *
   * @param fileKey
   * @param bucket
   * @param suffix
   * @param expire
   * @return
   */
  public String getUrlByQiniu(String fileKey, String bucket, String suffix, Long expire) {

    if (StringUtils.isEmpty(fileKey)) {
      return null;
    }
    try {
      String encodedFileName = URLEncoder.encode(fileKey, "utf-8").replaceAll("\\+", "%20");
      suffix = suffix != null ? "-" + suffix : "";
      String publicUrl = String.format("%s/%s", getDomainOfBucket(bucket), encodedFileName) + suffix;
      return auth.privateDownloadUrl(publicUrl, expire);
    } catch (Exception e) {
      logger.warn("Get image url error.", e);
    }
    return null;
  }

  private static String getDomainOfBucket(String bucket) {
    switch (bucket) {
      case Constants.Bucket.HUNTER_PHOTO:
        return Constants.QnDomain.HUNTER_PHOTO;
      case Constants.Bucket.MASTER_FILE:
        return Constants.QnDomain.MASTER_FILE;
      case Constants.Bucket.USER_AVATAR:
        return Constants.QnDomain.USER_AVATAR;
      case Constants.Bucket.FILE_PRIVATE:
        return Constants.QnDomain.FILE_PRIVATE;
      case Constants.Bucket.FISHTRIP_IMG:
        return Constants.QnDomain.FISHTRIP_IMG;
      case Constants.Bucket.FISHTRIP_IMG_PUBLIC:
        return Constants.QnDomain.FISHTRIP_IMG_PUBLIC;
      default:
        return Constants.QnDomain.FISHTRIP_IMG;
    }
  }


  private Container containerInit(String accountName) {
    Account account = accountInit(accountName);
    /*获取container容器信息 （开源包内部已做缓存）*/
    //自定义名称的container
    Container container =
        account.getContainer(generateContainerName());
    if (!container.exists()) {
      //prod 选中 iron 单副本 或 silver 双副本  //beta环境请设置"gold"
      if (env.equalsIgnoreCase("prod") || env.contains("prod")) {
        SwiftAccountEnum accountEnum = getAccountByAccountName(accountName);
        if (accountEnum == null) {
          return null;
        }
        if (!StringUtils.isEmpty(accountEnum.getLevel())) {
          container.setCustomHeaders(Collections.singletonList(new ContainerStoragePolicy(accountEnum.getLevel())));
        }
      } else {
        container.setCustomHeaders(Collections.singletonList(new ContainerStoragePolicy("gold")));

      }
      container.create();
      //设置容器为http可访问  如果不设置,只能通过api的形式进行访问
      container.makePublic();
    }
    return container;
  }

  private Account accountInit(String accountName) {
    AccountConfig config = new AccountConfig();
    config.setUsername(accountName + ":" + name);
    config.setPassword(password);
    config.setAuthUrl(authUrl);
    config.setAuthenticationMethod(AuthenticationMethod.TEMPAUTH);
    return new AccountFactory(config).createAccount();
  }

  private String generateContainerName() {
    LocalDate localDate = LocalDate.now();
    return String.format("day_%s", localDate.format(DateTimeFormatter.BASIC_ISO_DATE));
  }

  /**
   * 获取Swift上传token
   * token有效期 <=90min
   *
   * @param account
   * @return
   */
  public TokenInfo getToken(String account) {
    String signature =
        Hashing.sha1().hashBytes((apiKey + account + "v0.1" + System.currentTimeMillis() + secretKey).getBytes())
            .toString();
    String param = Base64.getEncoder().encodeToString(signature.getBytes());
    if (uploadUrl.endsWith("/")) {
      uploadUrl = uploadUrl.substring(0, uploadUrl.length() - 1);
    }
    String url = uploadUrl + "/getToken?" + param;
    OkHttpClient client = new OkHttpClient.Builder().connectTimeout(2, TimeUnit.SECONDS).build();
    Request request = new Request.Builder().url(url).get().build();

    Response response = null;
    try {
      response = client.newCall(request).execute();
      return new ObjectMapper().readValue(response.body().toString(), TokenInfo.class);

    } catch (IOException e) {
      logger.error("get token fail ", e);
    }
    return null;
  }

  private String formatSuffix(String suffix) {
    return Constants.SUFFIX_MAPPING.getOrDefault(suffix, suffix);
  }


}
