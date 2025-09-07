package com.arkioner.infrastructure.web;


import com.arkioner.domain.user.UserService;
import com.arkioner.infrastructure.security.JwtUtils;
import com.arkioner.infrastructure.web.dto.AuthResponse;
import com.arkioner.infrastructure.web.dto.LoginRequest;
import com.arkioner.infrastructure.web.dto.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtils jwtUtils;

    public AuthController(UserService userService, JwtUtils jwtUtils) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<String>> register(@RequestBody RegisterRequest req){
        return userService.register(req.username(), req.password(), req.avatarUrl())
                .map(u -> ResponseEntity.ok().body("OK"))
                .onErrorResume(ex -> Mono.just(ResponseEntity.badRequest().body(ex.getMessage())));
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<AuthResponse>> login(@RequestBody LoginRequest req) {
        return userService.authenticate(req.username(), req.password())
                .map(user -> {
                    String token = jwtUtils.generateToken(user.id(), user.username(), user.roles());
                    return ResponseEntity.ok(new AuthResponse(token, user.username(), user.avatarUrl()));
                })
                .switchIfEmpty(Mono.just(ResponseEntity.status(401).build()));
    }
}
