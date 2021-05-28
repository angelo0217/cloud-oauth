package com.auth.authorization.config.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

@Component("demoResponseExceptionTranslator")
public class DemoResponseExceptionTranslator implements WebResponseExceptionTranslator {
    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e){

        OAuth2Exception oAuth2Exception = (OAuth2Exception) e;

        return ResponseEntity
                .status(oAuth2Exception.getHttpErrorCode())
                .body(new DemoOauthException(oAuth2Exception.getMessage()));
    }
}