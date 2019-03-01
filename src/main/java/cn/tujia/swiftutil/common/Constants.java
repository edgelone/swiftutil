package cn.tujia.swiftutil.common;

import com.google.common.collect.ImmutableMap;

import java.util.HashSet;
import java.util.Set;

/**
 * @author lk
 * @date 2019/2/28
 */
public interface Constants {

  interface Bucket {
    String USER_AVATAR = "user-avatar";
    String FILE_PRIVATE = "file-private";
    String FISHTRIP_IMG = "fishtrip-img";
    String MASTER_FILE = "master-file";
    String HUNTER_PHOTO = "hunter-photo";
    String FISHTRIP_IMG_PUBLIC = "fishtrip-img-public";
  }

  interface QnDomain {
    String USER_AVATAR = "http://qn-user-avatar.fiiish.cn";
    String FILE_PRIVATE = "http://qn-file-private.fiiish.cn";
    String FISHTRIP_IMG = "http://qn-fiiish-img.fiiish.cn";
    String FISHTRIP_IMG_PUBLIC = "http://qn-fiiish-img-public.fiiish.cn";
    String MASTER_FILE = "http://qn-master-file.fiiish.cn";
    String HUNTER_PHOTO = "http://qn-hunter-photo.fiiish.cn";
  }

  ImmutableMap<String, String> SUFFIX_MAPPING = ImmutableMap.<String, String>builder()
      .put("oratelarge", "800_601")
      .put("ratetiny", "147_110")
      .put("thumb300x300", "300_300")
      .put("thumb400x300", "400_300")
      .put("thumb484x328", "484_328")
      .put("originwm", "1280_800")
      .put("ctripimg", "1500_1003")
      .put("tujiaimg", "1200_750")
      .put("tslarge_wm", "1600_1200")
      .put("tlarge_wm", "1024_768")
      .put("tsmall_wm", "640_480")
      .put("tmedium_wm", "800_600")
      .put("smalla", "370_230")
      .put("origina", "1921_1080")
      .put("thumb", "370_237")
      .put("originwm", "1920_1080")
      .put("smalla", "370_231")
      .put("origina", "1920_1081")
      .put("thumb", "200_75")
      .put("smalla", "370_232")
      .put("ratetiny", "147_110")
      .put("small", "100_100")
      .put("ratelarge", "800_601")
      .put("ratetiny", "147_110")
      .put("ratelarge", "800_601")
      .put("smalla", "370_230")
      .put("origina", "1921_1080").build();
}
