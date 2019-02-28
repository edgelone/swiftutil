package cn.tujia.swiftutil.enums;

/**
 * @author lk
 * @date 2019/2/28
 */
public enum SwiftAccountEnum {

  /**
   * 住宿房屋
   */
  ACCOMMODATIONIMAGE("accommodationimage", false, ""),
  /**
   * 头像
   */
  PORTRAITIMAGE("portraitimage", false, ""),
  /**
   * 评论图片
   */
  COMMENTIMAGE("commentimage", false, ""),
  /**
   * 文件类
   */
  FILEPUBLIC("filepublic", false, "silver"),
  /**
   * 私有文件
   */
  FILEPRIVATE("fileprivate", true, ""),
  /**
   * 认证图片
   */
  CERTIFICATIONIMAGE("certificationimage", true, "");

  SwiftAccountEnum(String accountName, boolean isPrivate, String level) {
    this.accountName = accountName;
    this.isPrivate = isPrivate;
    this.level = level;
  }

  public static SwiftAccountEnum getAccountByAccountName(String accountName) {
    for (SwiftAccountEnum e : SwiftAccountEnum.values()) {
      if (e.getAccountName().equalsIgnoreCase(accountName)) {
        return e;
      }
    }
    return null;
  }


  private String accountName;
  private boolean isPrivate;
  private String level;

  public String getAccountName() {
    return accountName;
  }

  public void setAccountName(String accountName) {
    this.accountName = accountName;
  }

  public boolean isPrivate() {
    return isPrivate;
  }

  public void setPrivate(boolean aPrivate) {
    isPrivate = aPrivate;
  }

  public String getLevel() {
    return level;
  }

  public void setLevel(String level) {
    this.level = level;
  }

}
