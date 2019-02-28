package cn.tujia.swiftutil.model;

import org.springframework.util.StringUtils;

/**
 * @author lk
 * @date 2019/2/28
 */
public class SwiftObject {
  private final static String prefixFlag = "upload/";

  private String account;
  private String container;
  private String swiftKey;
  private String suffixUrl;

  public SwiftObject(String swiftUrl, String suffix) {
    splitSwiftUrl(swiftUrl);
    String keyPre = swiftKey.substring(0, swiftKey.lastIndexOf("."));
    String keySuf = swiftKey.substring(swiftKey.lastIndexOf("."));
    if (StringUtils.isEmpty(suffix)) {
      return;
    }
    this.suffixUrl = "/" + account + "/" + container + "/thumb/" + keyPre + "_" + suffix + keySuf;
  }

  public SwiftObject(String swiftUrl) {
    splitSwiftUrl(swiftUrl);
  }

  private void splitSwiftUrl(String swiftUrl) {
    this.suffixUrl = swiftUrl;
    if (swiftUrl.startsWith("/")) {
      swiftUrl = swiftUrl.substring(1);
    }
    swiftUrl = swiftUrl.replace(prefixFlag, "");

    String[] keys = swiftUrl.split("/");
    this.account = keys[0];
    this.container = keys[1];
    this.swiftKey = keys[2];
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public String getContainer() {
    return container;
  }

  public void setContainer(String container) {
    this.container = container;
  }

  public String getSwiftKey() {
    return swiftKey;
  }

  public void setSwiftKey(String swiftKey) {
    this.swiftKey = swiftKey;
  }

  public String getSuffixUrl() {
    return suffixUrl;
  }

  public void setSuffixUrl(String suffixUrl) {
    this.suffixUrl = suffixUrl;
  }
}
