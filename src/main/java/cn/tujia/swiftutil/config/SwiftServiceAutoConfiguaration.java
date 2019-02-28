package cn.tujia.swiftutil.config;

import cn.tujia.swiftutil.SwiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lk
 * @date 2019/2/26
 */
@Configuration
@EnableConfigurationProperties(value = SwiftServiceProperties.class)
@ConditionalOnClass(SwiftService.class)
@ConditionalOnProperty(prefix = "swiftutil", value = "enable", matchIfMissing = true)
public class SwiftServiceAutoConfiguaration {
  @Autowired
  private SwiftServiceProperties swiftProps;
  @Value("${env}")
  private String ENV;

  @Bean
  @ConditionalOnMissingBean(SwiftService.class)
  public SwiftService swiftService() {

    return new SwiftService(swiftProps.getName(), swiftProps.getPassword(),
        swiftProps.getAuthUrl(), swiftProps.getQnAccessKey(), swiftProps.getQnSecretKey(), ENV,swiftProps.getDownloadUrl());
  }
}
