package com.miniblog.gateway.routes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

@Configuration
public class CommentServiceRouteConfig {
    @Value("${comment.service.url}")
    private String commentServiceUrl;

    @Value("${comment.service.path.pattern}")
    private String commentServicePathPattern;

    @Value("${springdoc.swagger-ui.urls[3].url}")
    private String commentServiceSwaggerUrl;

    @Bean
    public RouterFunction<ServerResponse> commentServiceRoute() {
        return GatewayRouterFunctions.route("comment_service")
                .route(RequestPredicates.path(commentServicePathPattern), HandlerFunctions.http(commentServiceUrl))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("commentServiceCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> commentServiceSwaggerRoute() {
        return GatewayRouterFunctions.route("comment_service_swagger")
                .route(RequestPredicates.path(commentServiceSwaggerUrl), HandlerFunctions.http(commentServiceUrl))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("commentServiceSwaggerCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .filter(FilterFunctions.setPath("/api-docs"))
                .build();
    }
}
