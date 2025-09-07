package com.arkioner.domain.user;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserProfileRepository {
    Mono<UserProfile> findByUserId(UUID userId);

    Mono<UserProfile> save(UserProfile userProfile);
}
