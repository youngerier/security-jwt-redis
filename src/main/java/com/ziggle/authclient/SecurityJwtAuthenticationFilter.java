package com.ziggle.authclient;

import com.google.common.base.Strings;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SecurityJwtAuthenticationFilter extends BasicAuthenticationFilter {
    private Logger log = LoggerFactory.getLogger(getClass());
    private static final String Bearer = "Bearer ";
    private ISecurityJwtTokenDecoder decoder;
    private SecurityJwtRedisConnection connection;
    private SecurityJwtProperties properties;

    public SecurityJwtAuthenticationFilter(AuthenticationManager authenticationManager, ISecurityJwtTokenDecoder decoder,
                                           SecurityJwtRedisConnection connection,
                                           SecurityJwtProperties properties) {
        super(authenticationManager);
        this.decoder = decoder;
        this.connection = connection;
        this.properties = properties;
    }

    public SecurityJwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                           AuthenticationEntryPoint authenticationEntryPoint) {
        super(authenticationManager, authenticationEntryPoint);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        if (authentication == null) {
            chain.doFilter(request, response);
        } else {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String header = request.getHeader(properties.getAuthHeader());
        if (!Strings.isNullOrEmpty(header)) {
            String token = connection.getUserInfo(header);
            if (token != null && !"".equals(token)) {
                try {
                    token = token.replaceFirst(Bearer, "");
                    // 在filter中引用spring context
                    SysUserDetail currentUser = decoder.getCurrentUserFromToken(token);
                    //set value to   Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
                    return new UsernamePasswordAuthenticationToken(currentUser, null, currentUser.getAuthorities());
                } catch (ExpiredJwtException e) {
                    log.error("Token已过期", e);
                    throw new SecurityJwtTokenException("Token已过期", e);
                } catch (UnsupportedJwtException e) {
                    log.error("Token格式错误", e);
                    throw new SecurityJwtTokenException("Token格式错误", e);
                } catch (MalformedJwtException e) {
                    log.error("Token没有被正确构造", e);
                    throw new SecurityJwtTokenException("Token没有被正确构造", e);
                } catch (SignatureException e) {
                    log.error("签名失败", e);
                    throw new SecurityJwtTokenException("签名失败", e);
                } catch (IllegalArgumentException e) {
                    log.error("非法参数异常", e);
                    throw new SecurityJwtTokenException("非法参数异常", e);
                }
            }
        }
        return null;
    }
}
