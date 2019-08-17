package com.ziggle.authclient;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SecurityJwtTokenDecoder implements ISecurityJwtTokenDecoder {

    private static final String CLAIM_KEY_AUTHORITIES = "role";
    private static final String CLAIM_KEY_ENABLE = "enable";
    private static final String Bearer = "Bearer";

    private final SecurityJwtProperties properties;
    private final LoadingCache<String, Claims> cache;

    private CacheLoader<String, Claims> cacheLoader() {
        return new CacheLoader<String, Claims>() {
            @Override
            public Claims load(String key) {
                return getAllClaimsFromToken(key);
            }
        };
    }

    public SecurityJwtTokenDecoder(SecurityJwtProperties properties) {
        this.properties = properties;
        this.cache = CacheBuilder.newBuilder()
                .maximumSize(3000)
                .expireAfterAccess(60L, TimeUnit.MINUTES)
                .build(cacheLoader());
    }

    @Override
    public SysUserDetail getCurrentUserFromToken(String token) {
        SysUserDetail user;
        try {
            Claims claims = cache.get(token);
            String username = claims.getSubject();
            List roles = (List) claims.get(CLAIM_KEY_AUTHORITIES);
            Boolean enable = (Boolean) claims.get(CLAIM_KEY_ENABLE);
            String id = claims.getId();
            Collection<? extends GrantedAuthority> authorities = parseArrayToAuthorities(roles);
            user = new SysUserDetail(Long.parseLong(id), username, "pa****rd", !enable, authorities);
            return user;
        } catch (Exception e) {
            throw new SecurityJwtTokenException(e.getMessage(), e);
        }
    }

    private Collection<? extends GrantedAuthority> parseArrayToAuthorities(List roles) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        SimpleGrantedAuthority authority;
        for (Object role : roles) {
            authority = new SimpleGrantedAuthority(role.toString());
            authorities.add(authority);
        }
        return authorities;
    }


    private Claims getAllClaimsFromToken(String token) {
        token = token.replace(Bearer, Strings.EMPTY).trim();
        return Jwts.parser()
                .setSigningKey(properties.getSigningKey())
                .parseClaimsJws(token)
                .getBody();
    }

}
