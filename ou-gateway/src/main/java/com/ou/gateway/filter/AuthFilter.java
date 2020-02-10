package com.ou.gateway.filter;

import java.util.List;
import java.util.function.Consumer;

import cn.hutool.core.util.StrUtil;
import com.ou.gateway.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * @author vince
 * @date 2019/12/16 22:18
 */
@Slf4j
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String secretKey = redisUtil.create("secretKey");
        log.info("secretKey: {}", secretKey);
        ServerHttpRequest request = exchange.getRequest().mutate()
                .headers(httpHeaders -> httpHeaders.remove("secretKey"))
                .build();
        HttpHeaders headers = exchange.getRequest().getHeaders();
        String authorization = headers.getFirst("Authorization");
        Consumer<HttpHeaders> httpHeaders = header -> {
            header.set("secretKey", secretKey);
            if (StrUtil.isNotBlank(authorization)) {
                header.set("Authorization", authorization);
            }
        };
        ServerHttpRequest newRequest = request.mutate().headers(httpHeaders).build();
        return chain.filter(exchange.mutate().request(newRequest).build());
    }

    @Override
    public int getOrder() {
        return -200;
    }
}
