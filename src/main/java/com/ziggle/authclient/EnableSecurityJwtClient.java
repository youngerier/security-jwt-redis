package com.ziggle.authclient;


import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableWebSecurity
@AutoConfigureBefore(SecurityAutoConfiguration.class)
@AutoConfigurationPackage
@Import({SecurityJwtConfigurationSelector.class}) // 导入依赖到ioc
public @interface EnableSecurityJwtClient {
}
