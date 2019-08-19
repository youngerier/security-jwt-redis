package com.ziggle.authclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.sync.RedisCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author: wp
 * @date: 2019-08-19 13:59
 */

public class SecurityJwtRedisConnection {
    private RedisCommands<String, String> cmd;
    private ObjectMapper om = new ObjectMapper();
    private final Logger log = LoggerFactory.getLogger(SecurityJwtRedisConnection.class);

    public SecurityJwtRedisConnection(SecurityJwtProperties properties) {
        RedisClient redisClient = RedisClient.create(RedisURI.create(properties.getRedisUrl()));
        this.cmd = redisClient.connect().sync();
    }


    public String getUserInfo(String key) {
        String s = cmd.get(key);
        try {
            if (!Strings.isNullOrEmpty(s)) {
                SecurityJwtToken securityJwtToken = om.readValue(s, SecurityJwtToken.class);
                return securityJwtToken.getToken();
            }
        } catch (IOException e) {
            // decode error
            log.error(e.getMessage(), e);
        }
        return "";
    }
}
