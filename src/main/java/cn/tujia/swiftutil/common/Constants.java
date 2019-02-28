package cn.tujia.swiftutil.common;

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
}
