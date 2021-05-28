package com.auth.authorization.controller;

import com.auth.authorization.model.AuthCode;
import com.auth.authorization.model.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TokenController {
    @Autowired
    private TokenStore tokenStore;

    @DeleteMapping("/revoke")
    public BaseResponse revokeToken(Authentication authentication){
        OAuth2AccessToken accessToken = tokenStore.getAccessToken((OAuth2Authentication) authentication);
        tokenStore.removeAccessToken(accessToken);
        return BaseResponse.success(AuthCode.SUCCESS);
    }
}
