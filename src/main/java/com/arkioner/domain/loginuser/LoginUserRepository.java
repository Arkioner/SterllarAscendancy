package com.arkioner.domain.loginuser;

import reactor.core.publisher.Mono;

public interface LoginUserRepository {
    Mono<LoginUser> findBy(String username);
    Mono<LoginUser> save(RegisterLoginUserCommand registerLoginUserCommand);
}

