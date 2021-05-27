package com.auth.authorization.config;

import com.auth.authorization.model.UserDetailVo;
import com.auth.authorization.service.UserClientService;
import com.auth.authorization.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig {


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;


    @Autowired
    private UserClientService userClientService;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;


    @Bean
    public AuthorizationServerConfigurerAdapter authorizationServerConfigurer() {
        return new AuthorizationServerConfigurerAdapter() {
            @Override
            public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
                endpoints.authenticationManager(authenticationManager)
                        .userDetailsService(userService)
                        .accessTokenConverter(jwtAccessTokenConverter())
                        .tokenStore(redisTokenStore())
                        .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
            }

            @Override
            public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
                clients.withClientDetails(userClientService);
            }

            @Override
            public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
                oauthServer.checkTokenAccess("isAuthenticated()");
            }
        };
    }
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter() {
            /***
             *  增強token的方法,自訂義一些token返回的信息
             */
            @Override
            public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
                String userName = authentication.getUserAuthentication().getName();

                UserDetailVo user = (UserDetailVo) authentication.getUserAuthentication().getPrincipal();// 登陸後放進去的UserDetail實現類一直查看link{SecurityConfiguration}
                /** 自訂義一些token屬性 ***/
                final Map<String, Object> additionalInformation = new HashMap<>();
                //add new type
                additionalInformation.put("userName", userName);
                //覆蓋預設不想被看到的屬性
                additionalInformation.put("authorities", null);
//                additionalInformation.put("roles", user.getAuthorities());
                ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);
                ((DefaultOAuth2AccessToken) accessToken).setScope(null);
                OAuth2AccessToken enhancedToken = super.enhance(accessToken, authentication);
                return enhancedToken;
            }

        };
        accessTokenConverter.setSigningKey("ASDFASFsdfsdfsdfsfadsf234asdfasfdas");//測試用，資源服務使用相同的字符達到一個對稱加密的效果，生產時候使用RSA非對稱加密方式
        return accessTokenConverter;
    }

//    @Bean
//    public JwtAccessTokenConverter jwtAccessTokenConverter() {
//        final JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
//        jwtAccessTokenConverter.setSigningKey("ASDFASFsdfsdfsdfsfadsf234asdfasfdas");
//        // 註解掉的原因是因為跟原本的一樣，但記錄一下如果需要特別調整可以在這調
//        //jwtAccessTokenConverter.setAccessTokenConverter(new CustomAccessTokenConverter());
//        return jwtAccessTokenConverter;
//    }
    @Bean
    @Qualifier("redisTokenStore")
    public TokenStore redisTokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

}