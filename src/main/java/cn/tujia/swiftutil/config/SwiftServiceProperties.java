package cn.tujia.swiftutil.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lk
 * @date 2019/2/26
 */
@ConfigurationProperties(prefix = "swiftutil")
public class SwiftServiceProperties {
  private String name;
  private String password;
  private String authUrl;
  private String uploadUrl;
  private String downloadUrl;
  private String apiKey;
  private String secretKey;
  private String qnAccessKey;
  private String qnSecretKey;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getAuthUrl() {
    return authUrl;
  }

  public void setAuthUrl(String authUrl) {
    this.authUrl = authUrl;
  }

  public String getUploadUrl() {
    return uploadUrl;
  }

  public void setUploadUrl(String uploadUrl) {
    this.uploadUrl = uploadUrl;
  }

  public String getDownloadUrl() {
    return downloadUrl;
  }

  public void setDownloadUrl(String downloadUrl) {
    this.downloadUrl = downloadUrl;
  }

  public String getApiKey() {
    return apiKey;
  }

  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

  public String getSecretKey() {
    return secretKey;
  }

  public void setSecretKey(String secretKey) {
    this.secretKey = secretKey;
  }

  public String getQnAccessKey() {
    return qnAccessKey;
  }

  public void setQnAccessKey(String qnAccessKey) {
    this.qnAccessKey = qnAccessKey;
  }

  public String getQnSecretKey() {
    return qnSecretKey;
  }

  public void setQnSecretKey(String qnSecretKey) {
    this.qnSecretKey = qnSecretKey;
  }
}
