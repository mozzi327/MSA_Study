package com.example.apigatewayservice.filter;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {

    public LoggingFilter() {
        super(Config.class);
    }

    /**
     * 커스텀 로깅 필터 구현(모노) - yml 설정 참조
     * @param config 커스텀 Config class
     * @return GatewayFilter
     */
    @Override
    public GatewayFilter apply(Config config) {
        // OrderedFilter 구현
        return new OrderedGatewayFilter((exchange, chain) -> {
                ServerHttpRequest request = exchange.getRequest();
                ServerHttpResponse response = exchange.getResponse();
                log.info("Logging Filter baseMessage : {}", config.getBaseMessage());

                if (config.isPreLogger())
                    log.info("Logging PRE Filter : requestId -> {}", request.getId());
                // Custom Post Filter
                return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                    if (config.isPostLogger())
                        log.info("Logging POST Filter : response code -> {}", response.getStatusCode());
                }));
        }, Ordered.LOWEST_PRECEDENCE); // HIGHEST_PRECEDENCE 옵션은 필터의 우선순위(가장 먼저)
//        }, Ordered.HIGHEST_PRECEDENCE); // HIGHEST_PRECEDENCE 옵션은 필터의 우선순위(가장 먼저)
    }

    @Getter
    public static class Config {
        // Put the configuration properties
        private final String baseMessage;
        private final boolean preLogger;
        private final boolean postLogger;

        @Builder
        public Config(String baseMessage, boolean preLogger, boolean postLogger) {
            this.baseMessage = baseMessage;
            this.preLogger = preLogger;
            this.postLogger = postLogger;
        }
    }
}
