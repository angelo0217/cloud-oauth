package com.auth.gw.config;

import cn.hutool.core.util.ArrayUtil;
import com.auth.gw.compoment.GwAuthenticationEntryPoint;
import com.auth.gw.compoment.GwAccessDeniedHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@AllArgsConstructor
@Configuration
@EnableWebFluxSecurity
public class ResourceServerConfig {
    private final AuthorizationManager authorizationManager;
    private final IgnoreUrlsConfig ignoreUrlsConfig;
    private final GwAccessDeniedHandler gwAccessDeniedHandler;
    private final GwAuthenticationEntryPoint gwAuthenticationEntryPoint;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange()
                .pathMatchers(ArrayUtil.toArray(ignoreUrlsConfig.getUrls(),String.class)).permitAll()//白名單配置
                .anyExchange().access(authorizationManager)//鑑權管理器配置
                .and().exceptionHandling()
                .accessDeniedHandler(gwAccessDeniedHandler)//處理未授權
                .authenticationEntryPoint(gwAuthenticationEntryPoint)//處理未認證
                .and().csrf().disable();
        return http.build();
    }

}