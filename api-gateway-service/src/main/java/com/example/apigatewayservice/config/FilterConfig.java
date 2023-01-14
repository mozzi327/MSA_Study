package com.example.apigatewayservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class FilterConfig {

    /**
     * yml 파일이 아닌 Java 코드로 Route 구현하기
     *
     * @param builder RouteLocatorBuilder
     * @return RouteLocator
     */
//    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                // route 1
                .route(r -> r.path("/first-service/**") // path 설정
                        .filters(f -> f.addRequestHeader("first-request", "first-request-header") // req 헤더 추가
                                .addResponseHeader("first-response", "first-response-header")) // res 헤더 추가
                        .uri("http://localhost:8081")) // 이동 주소
                // route 2
                .route(r -> r.path("/second-service/**") // path 설정
                        .filters(f -> f.addRequestHeader("second-request", "second-request-header") // req 헤더 추가
                                .addResponseHeader("second-response", "second-response-header")) // res 헤더 추가
                        .uri("http://localhost:8082")) // 이동 주소
                .build();
    }
}
