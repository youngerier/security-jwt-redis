package com.ziggle.authclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class SecurityJwtUsernamePasswordLoginFilter extends OncePerRequestFilter {
    Logger log = LoggerFactory.getLogger(SecurityJwtUsernamePasswordLoginFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain filterChain) throws ServletException, IOException {
        SecurityJwtResult res = new SecurityJwtResult();
        try {
            filterChain.doFilter(req, resp);
            //token exception
        } catch (SecurityJwtTokenException e) {
            log.error(e.getMessage(), e);
            res.setMessage(e.getMessage());
            convertObjectToJsonAndWriteResp(res, resp, HttpStatus.BAD_REQUEST);
            // other filter exception
        } catch (RuntimeException e) {
            res.setMessage(e.getMessage());
            convertObjectToJsonAndWriteResp(res, resp, HttpStatus.INTERNAL_SERVER_ERROR);
            log.error(e.getMessage(), e);
        }
    }

    private void convertObjectToJsonAndWriteResp(SecurityJwtResult object, HttpServletResponse resp, HttpStatus httpStatus) throws IOException {
        if (object == null) {
            return;
        }
        ObjectMapper mapper = new ObjectMapper();
        String s = mapper.writeValueAsString(object);
        resp.setStatus(httpStatus.value());
        resp.setCharacterEncoding(String.valueOf(StandardCharsets.UTF_8));
        resp.setContentType("application/json");
        resp.getWriter().write(s);
    }
}
