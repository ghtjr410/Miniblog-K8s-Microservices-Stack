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
public class ViewcountServiceRouteConfig {
    @Value("${viewcount.service.url}")
    private String viewcountServiceUrl;

    @Value("${viewcount.service.path.pattern}")
    private String viewcountServicePathPattern;

    @Value("${springdoc.swagger-ui.urls[5].url}")
    private String viewcountServiceSwaggerUrl;

    @Bean
    public RouterFunction<ServerResponse> viewcountServiceRoute() {
        return GatewayRouterFunctions.route("image_service")
                .route(RequestPredicates.path(viewcountServicePathPattern), HandlerFunctions.http(viewcountServiceUrl))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("imageServiceCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .build();
    }
    @Bean
    public RouterFunction<ServerResponse> viewcountServiceSwaggerRoute() {
        return GatewayRouterFunctions.route("image_service_swagger")
                .route(RequestPredicates.path(viewcountServiceSwaggerUrl), HandlerFunctions.http(viewcountServiceUrl))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("imageServiceSwaggerCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .filter(FilterFunctions.setPath("/api-docs"))
                .build();
    }
}
