package com.miniblog.gateway.config;

import com.miniblog.gateway.filter.CustomHeaderRemovalFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    // 인증 필요 없는 URL
    private final String[] freeResourceUrls = {"/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**",
            "/swagger-resources/**", "/api-docs/**", "/aggregate/**", "/actuator/health", "/actuator/prometheus", "/api/v1/query-service/**"};
    private final String[] userOnlyResourceUrls = {"/api/v1/image-service/**", "/api/v1/post-service/**", "/api/v1/like-service/**", "/api/v1/profile-service/**", "/api/v1/viewcount-service/**"};
    private final String[] adminOnlyResourceUrls = {"/api/v1/admin-service/**"};

    private final CustomHeaderRemovalFilter customHeaderRemovalFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(freeResourceUrls).permitAll()
                        .requestMatchers(userOnlyResourceUrls).hasRole("user")
                        .requestMatchers(adminOnlyResourceUrls).hasRole("admin")
                        .anyRequest().authenticated())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .addFilterBefore(customHeaderRemovalFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Collection<GrantedAuthority> authorities = extractRealmRoles(jwt.getClaimAsMap("realm_access"));
            return authorities;
        });
        return jwtAuthenticationConverter;
    }

    private Collection<GrantedAuthority> extractRealmRoles(Map<String, Object> realmAccess) {
        if (realmAccess == null || !realmAccess.containsKey("roles")) {
            return List.of();
        }
        List<String> roles = (List<String>) realmAccess.get("roles");
        return roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(Collectors.toList());
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.applyPermitDefaultValues(); // A
//        configuration.setAllowedOrigins(List.of("http://localhost:3000")); // B
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
//        configuration.setAllowedHeaders(List.of("*")); // B
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
