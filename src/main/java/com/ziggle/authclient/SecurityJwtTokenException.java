package com.ziggle.authclient;

public class SecurityJwtTokenException extends RuntimeException {
    public SecurityJwtTokenException(String message) {
        super(message);
    }

    public SecurityJwtTokenException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
