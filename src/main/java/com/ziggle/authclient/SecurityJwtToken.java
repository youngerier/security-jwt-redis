package com.ziggle.authclient;

import java.util.List;

/**
 * @author: wp
 * @date: 2019-08-19 14:26
 */

public class SecurityJwtToken {
    private Long id;
    private String username;
    private String token;
    private Boolean enabled;
    private List<Auth> authorities;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<Auth> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Auth> authorities) {
        this.authorities = authorities;
    }
}

class Auth{
    private String authority;

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
