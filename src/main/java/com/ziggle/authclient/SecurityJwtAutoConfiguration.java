package com.ziggle.authclient;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnClass(EnableSecurityJwtClient.class)
@EnableConfigurationProperties(SecurityJwtProperties.class)
@Import({SecurityJwtConfig.class})
public class SecurityJwtAutoConfiguration {
}

