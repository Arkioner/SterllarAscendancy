package com.arkioner.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    private final String secret = "14747a4a-e0b7-401a-9f11-d73b733b4fa5"; // keep in application.yml/env

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/api/auth/**").permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(this::jwtToAuth)));

        return http.build();
    }

    private Mono<AbstractAuthenticationToken> jwtToAuth(Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getClaimAsString("jti")); // we’ll use `sub` claim as userId
        return Mono.just(new JwtAuthentication(userId, jwt, Collections.emptyList()));
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withSecretKey(
                new javax.crypto.spec.SecretKeySpec(secret.getBytes(), "HmacSHA256")
        ).build();
    }

    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() {
        return NimbusReactiveJwtDecoder.withSecretKey(
                new javax.crypto.spec.SecretKeySpec(secret.getBytes(), "HmacSHA256")
        ).build();
    }

    // custom token that exposes userId
    public static class JwtAuthentication extends AbstractAuthenticationToken {
        private final UUID userId;
        private final Jwt jwt;

        public JwtAuthentication(UUID userId, Jwt jwt, Collection<? extends GrantedAuthority> authorities) {
            super(authorities);
            this.userId = userId;
            this.jwt = jwt;
            setAuthenticated(true);
        }

        @Override
        public Object getCredentials() { return jwt; }

        @Override
        public Object getPrincipal() { return userId; }

        public UUID getUserId() { return userId; }
    }
}
