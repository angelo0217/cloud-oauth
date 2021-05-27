package com.auth.gw.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Slf4j
@Component
public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {


    @Autowired
    @Qualifier("stringBytesRedisTemplate")
    private RedisTemplate<String, byte[]> redisTemplate;

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
        ServerWebExchange exchange = authorizationContext.getExchange();
        String authorizationToken = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if(!StringUtils.isEmpty(authorizationToken)){
            try {
                String token = authorizationToken.replace("Bearer ", "");
                byte[] bytes = redisTemplate.opsForValue().get("access:" + token);
                if (bytes != null && bytes.length > 0) {
                    return Mono.just(new AuthorizationDecision(true));
                }
            } catch (Exception ex){
                log.error("get redis error: ", ex);
            }
        }

        return Mono.just(new AuthorizationDecision(false));
    }

//    @Override
//    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
//        System.out.println("~~~~~~~~~~~~~~~~~~~~check");
//        List<String> authorities = new ArrayList<>();
//        authorities.add("admin");
//        authorities = authorities.stream().map(i -> i = "ROLE_" + i).collect(Collectors.toList());
//        authorities.add("test");
//        //认证通过且角色匹配的用户可访问当前路径
//        return mono
//                .filter(Authentication::isAuthenticated)
//                .flatMapIterable(Authentication::getAuthorities)
//                .map(GrantedAuthority::getAuthority)
//                .any(authorities::contains)
//                .map(AuthorizationDecision::new)
//                .defaultIfEmpty(new AuthorizationDecision(false));
//
//    }

}