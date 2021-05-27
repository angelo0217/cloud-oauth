package com.auth.gw.filer;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Slf4j
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (StringUtils.isEmpty(token)) {
            return chain.filter(exchange);
        }
        try {
//          解析token放到後端請求header
            SecretKey secretKey = new SecretKeySpec("ASDFASFsdfsdfsdfsfadsf234asdfasfdas".getBytes(), SignatureAlgorithm.HS256.getJcaName());
            String realToken = token.replace("Bearer ", "");
            Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(realToken).getBody();
            String userName = (String) claims.get("user_name");
            log.info("token: {}, user: {}", realToken, userName);

            ServerHttpRequest request = exchange.getRequest().mutate().header("user_name", userName).build();
            exchange = exchange.mutate().request(request).build();
        } catch (Exception e) {
            log.error("error: ", e);
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}