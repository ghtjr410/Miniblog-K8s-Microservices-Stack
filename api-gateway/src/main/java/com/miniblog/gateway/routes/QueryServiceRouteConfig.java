package com.miniblog.gateway.routes;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions;
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

    @Value("${springdoc.swagger-ui.urls[2].url}")
    private String queryServiceSwaggerUrl;

    @Bean
    public RouterFunction<ServerResponse> queryServiceRoute() {
        return GatewayRouterFunctions.route("query_service")
                .route(RequestPredicates.path(queryServicePathPattern), HandlerFunctions.http(queryServiceUrl))
                .build();
    }
    @Bean
    public RouterFunction<ServerResponse> queryServiceSwaggerRoute() {
        return GatewayRouterFunctions.route("query_service_swagger")
                .route(RequestPredicates.path(queryServiceSwaggerUrl), HandlerFunctions.http(queryServiceUrl))
                .filter(FilterFunctions.setPath("/api-docs"))
                .build();
    }
}
