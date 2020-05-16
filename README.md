# swiftutil

# 上传/下载swift文件工具类

- 兼容qiniu上传下载功能
- 需要在主项目 application.yml里配置对应的密钥和账户信息

# 使用方式

**注意:需要设置maven仓库为fiiish**

在pom文件中加入依赖
```xml
  <dependency>
     <groupId>com.tujia.os</groupId>
     <artifactId>swiftutil-spring-boot-starter</artifactId>
     <version>1.0-SNAPSHOT</version>
  </dependency>
```


# 配置方式

```yaml
env: dev

swiftutil:
  name: "name"
  password: "passwd"
  auth-url: "http://ttttt.com/auth/v1.0"
  qn-access-key: jfdiosajfdkjkLJJKGHJKGHJ
  qn-secret-key: dsadasdsa-SJIOJKLJFJ
  download-url: "https://pic.fvt.tujia.com"
  upload-url: "https://upload.fvt.tujia.com"
  api-key: dsjaoidhsna
  secret-key: djioasfnsanl
```

# 说明
- swiftUrl 指通过新的方式上传至swift存储系统中的文件标识,作用相当于原先的 fileKey+bucket
- swift 新存储系统(架构),使用openstack 开源框架 swift
- joss swift对应的java sdk
- 中间件 指为了实现切图处理和图片管理等功能使用java开发的一套系统,一般App端和PC端上传都会通过中间件,拿取切图也是通过g该中间件实现
- token 一般指通过中间件获取到的account限定权限,通过token可以上传和下载文件
- SDK上传 指服务端系统通过本SDK直连swift存储系统上传文件,不经过中间件

# 使用规范

- 服务端上传非图片类文件
  - 可使用的account为 **osfilepublic** 和 **osfileprivate**,前者可使用http访问,后者必须获取token才可使用http访问
  - 上传临时文件或非重要文件 需指定存在时间 参数名为 expire
  - 若是重要文件,需永久保存,传null即可
```java
 /**
   * 上传文件
   * 兼容qiniu和swift
   *
   * @param accountName
   * @param inputStream
   * @param suffix
   * @param expire      有效时间 秒
   * @return
   */
  public String upload(String accountName, String bucket, String fileName, InputStream inputStream, String suffix, Long expire) {
    if (StringUtils.isEmpty(accountName) && StringUtils.isEmpty(bucket)) {
      return null;
    }
```
- 服务端上传图片类文件
  - 可使用的account 为 **ostenement**(住宿房屋类),**oscentified**(认证类,敏感类),**ospicture**(其他内容图片)
  - 图片命名规则为 年月日时分秒四位随机数,如 201811121344576789.xx , **.xx** 为文件后缀,支持绝大多数图片文件,除了BMP,gif,HEIC
  - 图片使用该SDK内部上传方法为直连swift对象存储系统上传,与端上传不同
  - 通过SDK获取的token,为HTTP上传文件和HTTP读取敏感文件使用,有效期不大于一个半小时,可选是否回调,若回调,则会将对应图片信息写入antman系统,文件类必须配置不可回调,否则上传必定失败
- 原七牛样式说明
  - 通过SDK内的getUrl()方法,可传入原七牛filekey,bucket,suffix来获取切图,也可以通过swifturl,suffix来获取新存储系统中的切图
  - 新存储的图片样式名均为分辨率表示,如```https://pic1.fvt.tujia.com/upload/ostenement/day_190306/thumb/201903061656037922_640_480.jpg```
  - 原有样式名仍然提供兼容,SDK内部会做转换
  ```java
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
      .put("thumb", "370_231")
      .put("smalla", "370_231")
      .put("origina", "1500_1003")
      .put("small", "370_231")
      .put("ratelarge", "800_601")
  ```
  
