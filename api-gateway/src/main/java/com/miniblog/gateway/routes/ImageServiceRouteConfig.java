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
public class ImageServiceRouteConfig {
    @Value("${image.service.url}")
    private String imageServiceUrl;

    @Value("${image.service.path.pattern}")
    private String imageServicePathPattern;

    @Value("${springdoc.swagger-ui.urls[0].url}")
    private String imageServiceSwaggerUrl;

    @Bean
    public RouterFunction<ServerResponse> imageServiceRoute() {
        return GatewayRouterFunctions.route("image_service")
                .route(RequestPredicates.path(imageServicePathPattern), HandlerFunctions.http(imageServiceUrl))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("imageServiceCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .build();
    }
    @Bean
    public RouterFunction<ServerResponse> imageServiceSwaggerRoute() {
        return GatewayRouterFunctions.route("image_service_swagger")
                .route(RequestPredicates.path(imageServiceSwaggerUrl), HandlerFunctions.http(imageServiceUrl))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("imageServiceSwaggerCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .filter(FilterFunctions.setPath("/api-docs"))
                .build();
    }
}
