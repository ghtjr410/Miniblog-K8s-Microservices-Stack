package com.miniblog.gateway.routes;

import com.miniblog.gateway.filter.JwtHeaderFilter;
import com.miniblog.gateway.util.HeaderType;
import lombok.RequiredArgsConstructor;
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
import java.util.EnumSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class LikeServiceRouteConfig {
    @Value("${like.service.url}")
    private String likeServiceUrl;

    @Value("${like.service.path.pattern}")
    private String likeServicePathPattern;

    @Value("${springdoc.swagger-ui.urls[4].url}")
    private String likeServiceSwaggerUrl;

    private final JwtHeaderFilter jwtHeaderFilter;

    @Bean
    public RouterFunction<ServerResponse> likeServiceRoute() {
        Set<HeaderType> headersToInclude = EnumSet.of(HeaderType.SUB);

        return GatewayRouterFunctions.route("like_service")
                .route(RequestPredicates.path(likeServicePathPattern), HandlerFunctions.http(likeServiceUrl))
                .filter(jwtHeaderFilter.addJwtHeadersFilter(headersToInclude))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("likeServiceCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .build();
    }
    @Bean
    public RouterFunction<ServerResponse> likeServiceSwaggerRoute() {
        return GatewayRouterFunctions.route("like_service_swagger")
                .route(RequestPredicates.path(likeServiceSwaggerUrl), HandlerFunctions.http(likeServiceUrl))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("likeServiceSwaggerCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .filter(FilterFunctions.setPath("/api-docs"))
                .build();
    }

}
