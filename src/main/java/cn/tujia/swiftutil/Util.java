package cn.tujia.swiftutil;

import org.springframework.util.StringUtils;

/**
 * @author lk
 * @date 2019/3/1
 */
public class Util {
  public static String combainUrlPart(String first, String... args) {
    if (StringUtils.isEmpty(first) || first.equals("/")) {
      first = "/";
    } else if (first.endsWith("/")) {
      first = first.substring(0, first.length() - 1);
    }
    StringBuilder sb = new StringBuilder(first);
    for (String a : args) {
      if (a.endsWith("/")) {
        a = a.substring(0, first.length() - 1);
      }
      if (a.startsWith("/")) {
        sb.append(a);
      } else {
        sb.append("/").append(a);
      }
    }
    return sb.toString();
  }
}
