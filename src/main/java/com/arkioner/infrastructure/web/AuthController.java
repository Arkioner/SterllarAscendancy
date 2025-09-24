package com.arkioner.infrastructure.web;


import com.arkioner.domain.loginuser.LoginUser;
import com.arkioner.domain.loginuser.LoginUserService;
import com.arkioner.domain.loginuser.UsernameAlreadyInUseException;
import com.arkioner.infrastructure.security.JwtUtils;
import com.arkioner.infrastructure.web.dto.AuthResponse;
import com.arkioner.infrastructure.web.dto.LoginRequest;
import com.arkioner.infrastructure.web.dto.RegisterRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final LoginUserService loginUserService;
    private final JwtUtils jwtUtils;

    public AuthController(LoginUserService loginUserService, JwtUtils jwtUtils) {
        this.loginUserService = loginUserService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<AuthResponse>> register(@RequestBody RegisterRequest req){
        return loginUserService.register(req.username(), req.password())
                .map(this::buildAuthResponse)
                .map(authResponse -> ResponseEntity.status(HttpStatus.CREATED).body(authResponse));
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<AuthResponse>> login(@RequestBody LoginRequest req) {
        return loginUserService.authenticate(req.username(), req.password())
                .map(this::buildAuthResponse)
                .map(ResponseEntity::ok);
    }

    private AuthResponse buildAuthResponse(LoginUser loginUser) {
        String token = jwtUtils.generateToken(loginUser);
        return new AuthResponse(token);
    }

    @ExceptionHandler
    public Mono<ResponseEntity<String>> handleException(UsernameAlreadyInUseException ex) {
        return Mono.just(ResponseEntity.unprocessableEntity().body(ex.getMessage()));
    }

}
