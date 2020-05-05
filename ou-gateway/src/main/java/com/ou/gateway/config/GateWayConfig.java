package com.ou.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author vince
 * @date 2019/12/18 10:28
 */
@Configuration
public class GateWayConfig {

    /**
     * 配置路由转发规则
     */
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/api/v1/system/**").uri("lb://ou-system"))
                .route(r -> r.path("/api/v1/auth/**").uri("lb://ou-system"))
                .route(r -> r.path("/api/v1/util/**").uri("lb://ou-util"))
                .build();

    }

}
