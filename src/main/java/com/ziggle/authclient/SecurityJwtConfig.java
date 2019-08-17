package com.ziggle.authclient;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class SecurityJwtConfig {

    private static final String[] AUTH_WHITE_LIST = {
            // -- swagger ui
            "/swagger-resources/**",
            "/v2/api-docs",
            "/swagger-ui.html",
            "/**/*.css",
            "/**/*.js",
            "/**/*.png",
            "/**/*.jpg",
            "/webjars/**",
            "/druid/**",
            "/oucloud/api/v1/user/reset_mail",
            "/oucloud/api/v1/user/reset_password",
            "/oucloud/api/v1/terminal/register",
            "/oucloud/api/v1/terminal/login",
            "/actuator/**",
            "/oucloud/api/v1/access_user/**",
            "/oucloud/api/v1/sixtwo",
            "/oucloud/api/v1/sixtwo/queryids",
    };

    @Bean
    @ConditionalOnClass(EnableWebSecurity.class)
    public WebSecurityConfigurerAdapter webSecurityConfigurerAdapter(SecurityJwtProperties props, ISecurityJwtTokenDecoder decoder) {

        return new WebSecurityConfigurerAdapter() {

            @Override
            protected void configure(HttpSecurity http) throws Exception {
                String[] arr;
                if (props.getAuthWhiteLists() == null || props.getAuthWhiteLists().isEmpty()) {
                    arr = AUTH_WHITE_LIST;
                } else {
                    arr = props.getAuthWhiteLists().toArray(new String[0]);
                }

                http
                        .cors()
                        .and().csrf().disable()
                        .sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .and()
                        .authorizeRequests()
                        // 预留注册地址 /sys-user/login 的POST请求 都放行
                        .antMatchers(HttpMethod.POST, props.getRoutingPrefix() + "/sys_user/login").permitAll()
                        .antMatchers(HttpMethod.POST, props.getRoutingPrefix() + "/user/").permitAll()
                        // 白名单
                        .antMatchers(arr).permitAll()
                        // 所有请求需要身份认证
                        .anyRequest().authenticated()
                        .and()
                        // 认证前添加 token exception handler
                        .addFilterBefore(new SecurityJwtUsernamePasswordLoginFilter(), ChannelProcessingFilter.class)
//                        .addFilterAfter(new SecurityJwtUsernamePasswordLoginFilter(), UsernamePasswordAuthenticationFilter.class)
                        .addFilterBefore(new SecurityJwtAuthenticationFilter(authenticationManager(), decoder), UsernamePasswordAuthenticationFilter.class)
                ;
            }


        };
    }

}
