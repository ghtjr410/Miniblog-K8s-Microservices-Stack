package com.miniblog.gateway.routes;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
@RequiredArgsConstructor
public class QueryServiceRouteConfig {
    @Value("${query.service.url}")
    private String queryServiceUrl;

    @Value("${query.service.path.pattern}")
    private String queryServicePathPattern;

    @Bean
    public RouterFunction<ServerResponse> queryServiceRoute() {
        return GatewayRouterFunctions.route("query_service")
                .route(RequestPredicates.path(queryServicePathPattern), HandlerFunctions.http(queryServiceUrl))
                .build();
    }
}
