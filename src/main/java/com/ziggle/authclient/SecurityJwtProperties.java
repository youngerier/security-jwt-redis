package com.ziggle.authclient;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;


@Validated
@ConfigurationProperties(prefix = "com.ziggle.security")
public class SecurityJwtProperties {


    private List<String> authWhiteLists;
    private String routingPrefix;


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

}
