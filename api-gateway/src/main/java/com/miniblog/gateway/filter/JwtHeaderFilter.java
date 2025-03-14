package com.miniblog.gateway.filter;

import com.miniblog.gateway.util.HeaderType;
import com.miniblog.gateway.util.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class JwtHeaderFilter {
    public HandlerFilterFunction<ServerResponse, ServerResponse> addJwtHeadersFilter(Set<HeaderType> headersToInclude) {
        return (request, next) -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
                
                Jwt jwt = (Jwt) authentication.getPrincipal();
                HeaderMapRequestWrapper requestWrapper = new HeaderMapRequestWrapper(request.servletRequest());

                for (HeaderType headerType : headersToInclude) {
                    switch (headerType) {
                        case SUB:
                            String sub = jwt.getSubject();
                            requestWrapper.addHeader(headerType.getHeaderName(), sub);
                            break;
                        case ROLES:
                            Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
                            List<String> roles = JwtUtil.extractRealmRoles(realmAccess);
                            String rolesHeader = String.join(",", roles);
                            requestWrapper.addHeader(headerType.getHeaderName(), rolesHeader);
                            break;
                    }
                }

                ServerRequest newRequest = ServerRequest.create(requestWrapper, request.messageConverters());

                return next.handle(newRequest);
            } else {
                return next.handle(request);
            }
        };
    }
}