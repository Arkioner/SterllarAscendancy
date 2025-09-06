package com.arkioner.infrastructure.security;

import com.arkioner.domain.user.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class SecurityConfig {

    private final JwtUtils jwtUtils;
    private final UserService userService;

    public SecurityConfig(JwtUtils jwtUtils, UserService userService) {
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .authorizeExchange()
                .pathMatchers("/api/auth/**", "/actuator/**").permitAll()
                .anyExchange().authenticated()
                .and()
                .authenticationManager(reactiveAuthenticationManager())
                .addFilterAt((exchange, chain) -> {
                    String auth = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                    if (auth != null && auth.startsWith("Bearer ")) {
                        String token = auth.substring(7);
                        try {
                            Claims claims = jwtUtils.parse(token).getBody();
                            String username = claims.getSubject();
                            List<String> roles = claims.get("roles", List.class);
                            var authToken = new UsernamePasswordAuthenticationToken(username, token,
                                    roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
                            return reactiveAuthenticationManager().authenticate(authToken).flatMap(a -> chain.filter(exchange));
                        } catch (Exception ex) {
                            return chain.filter(exchange); // unauthenticated -> will be rejected if access to endpoint requires auth
                        }
                    }
                    return chain.filter(exchange);
                }, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager() {
        return authentication -> {
            String principal = (String) authentication.getPrincipal();
            // Principal here is username (token's subject)
            return userService.findById(null) // we won't load user here; simple pass-through if token valid
                    .then(Mono.just(authentication)); // For now, let token presence be enough since JwtUtils parsing already validates signature
        };
    }
}
