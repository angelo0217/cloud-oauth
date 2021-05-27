package com.auth.authorization.model;


import com.auth.authorization.utils.OauthClientUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ClientDetailVO implements ClientDetails {
    private String account;
    private String pwd;
    private Integer tokenExpireTime;
    private Integer refreshTokenExpireTime;

    public ClientDetailVO(String account, String pwd, Integer tokenExpireTime, Integer refreshTokenExpireTime){
        this.account = account;
        this.pwd = pwd;
        this.tokenExpireTime = tokenExpireTime;
        this.refreshTokenExpireTime = refreshTokenExpireTime;

    }

    @Override
    public String getClientId() {
        return this.account;
    }

    @Override
    public Set<String> getResourceIds() {
        return OauthClientUtils.transformStringToSet("testOauth",String.class);
    }

    @Override
    public boolean isSecretRequired() {
        return true;
    }

    @Override
    public String getClientSecret() {
        return this.pwd;
    }

    @Override
    public boolean isScoped() {
        return true;
    }

    @Override
    public Set<String> getScope() {
        return OauthClientUtils.transformStringToSet("read,write",String.class);
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        return OauthClientUtils.transformStringToSet("refresh_token,authorization_code,password",String.class);
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        Set<String> strings = new HashSet<>();
        strings.add("https://www.youtube.com/");
        return strings;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN");
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return tokenExpireTime;
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return refreshTokenExpireTime;
    }

    @Override
    public boolean isAutoApprove(String scope) {
        return true;
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return null;
    }
}
