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
public class ProfileServiceRouteConfig {
    @Value("${profile.service.url}")
    private String profileServiceUrl;
    @Value("${profile.service.path.pattern}")
    private String profileServicePathPattern;
    @Value("${springdoc.swagger-ui.urls[6].url}")
    private String profileServiceSwaggerUrl;

    private final JwtHeaderFilter jwtHeaderFilter;

    @Bean
    public RouterFunction<ServerResponse> profileServiceRoute() {
        Set<HeaderType> headersToInclude = EnumSet.of(HeaderType.SUB);

        return GatewayRouterFunctions.route("profile_service")
                .route(RequestPredicates.path(profileServicePathPattern), HandlerFunctions.http(profileServiceUrl))
                .filter(jwtHeaderFilter.addJwtHeadersFilter(headersToInclude))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("profileServiceCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .build();
    }
    @Bean
    public RouterFunction<ServerResponse> profileServiceSwaggerRoute() {
        return GatewayRouterFunctions.route("profile_service_swagger")
                .route(RequestPredicates.path(profileServiceSwaggerUrl), HandlerFunctions.http(profileServiceUrl))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("profileServiceCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .filter(FilterFunctions.setPath("/api-docs"))
                .build();
    }
}
