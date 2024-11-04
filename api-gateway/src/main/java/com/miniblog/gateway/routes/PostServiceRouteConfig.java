package com.miniblog.gateway.routes;

import com.miniblog.gateway.filter.JwtHeaderFilter;
import com.miniblog.gateway.util.HeaderType;
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

import java.util.EnumSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class PostServiceRouteConfig {
    @Value("${post.service.url}")
    private String postServiceUrl;

    @Value("${post.service.path.pattern}")
    private String postServicePathPattern;

    @Value("${springdoc.swagger-ui.urls[1].url}")
    private String postServiceSwaggerUrl;

    private final JwtHeaderFilter jwtHeaderFilter;

    @Bean
    public RouterFunction<ServerResponse> postServiceRoute() {
        Set<HeaderType> headersToInclude = EnumSet.of(HeaderType.SUB);

        return GatewayRouterFunctions.route("post_service")
                .route(RequestPredicates.path(postServicePathPattern), HandlerFunctions.http(postServiceUrl))
                .filter(jwtHeaderFilter.addJwtHeadersFilter(headersToInclude))
                .build();
    }
    @Bean
    public RouterFunction<ServerResponse> postServiceSwaggerRoute() {
        return GatewayRouterFunctions.route("post_service_swagger")
                .route(RequestPredicates.path(postServiceSwaggerUrl), HandlerFunctions.http(postServiceUrl))
                .filter(FilterFunctions.setPath("/api-docs"))
                .build();
    }
}
