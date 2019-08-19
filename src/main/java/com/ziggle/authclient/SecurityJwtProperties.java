package com.ziggle.authclient;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;


@Validated
@ConfigurationProperties(prefix = "com.ziggle.security")
public class SecurityJwtProperties {


    private List<String> authWhiteLists;
    private String routingPrefix;
    private String signingKey;
    /**
     * redis://7JynNEZ8@115.239.209.191:6379/5
     */
    private String redisUrl;
    private String authHeader;


    public String getSigningKey() {
        return signingKey;
    }

    public void setSigningKey(String signingKey) {
        this.signingKey = signingKey;
    }


    public String getRoutingPrefix() {
        return routingPrefix;
    }

    public void setRoutingPrefix(String routingPrefix) {
        this.routingPrefix = routingPrefix;
    }

    public List<String> getAuthWhiteLists() {
        return authWhiteLists;
    }

    public void setAuthWhiteLists(List<String> authWhiteLists) {
        this.authWhiteLists = authWhiteLists;
    }

    public String getRedisUrl() {
        return redisUrl;
    }

    public void setRedisUrl(String redisUrl) {
        this.redisUrl = redisUrl;
    }

    public String getAuthHeader() {
        return authHeader;
    }

    public void setAuthHeader(String authHeader) {
        this.authHeader = authHeader;
    }
}
