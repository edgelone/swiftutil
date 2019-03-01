package cn.tujia.swiftutil.model;

/**
 * @author lk
 * @date 2019/3/1
 */
public class TokenInfo {
  private String token;
  private Long expireTime;

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Long getExpireTime() {
    return expireTime;
  }

  public void setExpireTime(Long expireTime) {
    this.expireTime = expireTime;
  }
}
