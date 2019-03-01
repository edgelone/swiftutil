# swiftutil

# 上传/下载swift文件工具类

- 兼容qiniu上传下载功能
- 需要在主项目 application.yml里配置对应的密钥和账户信息

# 使用方式

**注意:需要设置maven仓库为fiiish或tujia**

在pom文件中加入依赖
```xml
  <dependency>
     <groupId>cn.tujia</groupId>
     <artifactId>swiftutil-spring-boot-starter</artifactId>
     <version>1.0</version>
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


