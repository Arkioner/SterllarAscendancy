package com.arkioner.domain.user;

import reactor.core.publisher.Mono;

import java.util.UUID;

public class UserProfileService {
    private final UserProfileRepository userProfileRepository;

    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    public Mono<UserProfile> getProfile(UUID userId) {
        return userProfileRepository.findByUserId(userId);
    }

    public Mono<UserProfile> updateProfile(UserProfile newUserProfile) {
        return userProfileRepository.save(newUserProfile);
    }
}

