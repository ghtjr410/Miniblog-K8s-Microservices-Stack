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
public class AccountServiceRouteConfig {
    @Value("${account.service.url}")
    private String accountServiceUrl;

    @Value("${account.service.path.pattern}")
    private String accountServicePathPattern;

    @Value("${springdoc.swagger-ui.urls[1].url}")
    private String accountServiceSwaggerUrl;

    private final JwtHeaderFilter jwtHeaderFilter;

    @Bean
    public RouterFunction<ServerResponse> accountServiceRoute() {
        Set<HeaderType> headersToInclude = EnumSet.of(HeaderType.SUB);

        return GatewayRouterFunctions.route("account_service")
                .route(RequestPredicates.path(accountServicePathPattern), HandlerFunctions.http(accountServiceUrl))
                .filter(jwtHeaderFilter.addJwtHeadersFilter(headersToInclude))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("accountServiceCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> accountServiceSwaggerRoute() {
        return GatewayRouterFunctions.route("account_service_swagger")
                .route(RequestPredicates.path(accountServiceSwaggerUrl), HandlerFunctions.http(accountServiceUrl))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("accountServiceCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .filter(FilterFunctions.setPath("/api-docs"))
                .build();
    }
}
