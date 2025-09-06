package com.arkioner.domain.user;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserRepository {
    Mono<User> findByUsername(String username);

    Mono<User> findById(UUID id);

    Mono<User> save(User user);
}

