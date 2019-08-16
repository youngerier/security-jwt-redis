package com.ziggle.authclient;

public interface ISecurityJwtTokenDecoder {
    /**
     * 从token中获取信息
     *
     * @param token
     * @return
     */
    SysUserDetail getCurrentUserFromToken(String token);
}
