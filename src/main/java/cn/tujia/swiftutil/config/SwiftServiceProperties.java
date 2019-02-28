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
  private String downloadUrl;
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

  public String getDownloadUrl() {
    return downloadUrl;
  }

  public void setDownloadUrl(String downloadUrl) {
    this.downloadUrl = downloadUrl;
  }
}
