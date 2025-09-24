package com.arkioner.domain.loginuser;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import reactor.core.publisher.Mono;

import java.util.UUID;

public class LoginUserService {

    private final LoginUserRepository loginUserRepository;
    private final BCryptPasswordEncoder pwEncoder = new BCryptPasswordEncoder();

    public LoginUserService(LoginUserRepository loginUserRepository) {
        this.loginUserRepository = loginUserRepository;
    }

    public Mono<LoginUser> register(String username, String password) {
        return loginUserRepository.findBy(username)
                .flatMap(existing -> Mono.<LoginUser>error(new UsernameAlreadyInUseException(username)))
                .switchIfEmpty(Mono.defer(() -> {
                    String hash = pwEncoder.encode(password);
                    RegisterLoginUserCommand u = new RegisterLoginUserCommand(LoginUserId.generate(), username, hash);
                    return loginUserRepository.save(u);
                }));
    }

    public Mono<LoginUser> authenticate(String username, String password) {
        return loginUserRepository.findBy(username)
                .flatMap(u -> {
                    boolean ok = pwEncoder.matches(password, u.passwordHash());
                    return ok ? Mono.just(u) : Mono.empty();
                });
    }
}
