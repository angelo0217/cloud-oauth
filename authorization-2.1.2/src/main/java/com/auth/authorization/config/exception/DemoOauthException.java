package com.auth.authorization.config.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

@JsonSerialize(using = DemoOauthExceptionSerializer.class)
public class DemoOauthException extends OAuth2Exception {
    public DemoOauthException(String msg) {
        super(msg);
    }
}