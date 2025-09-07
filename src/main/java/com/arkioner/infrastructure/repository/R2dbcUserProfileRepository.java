package com.arkioner.infrastructure.repository;

import com.arkioner.domain.user.UserProfile;
import com.arkioner.domain.user.UserProfileRepository;
import com.arkioner.infrastructure.repository.dto.UserProfileDbo;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public class R2dbcUserProfileRepository implements UserProfileRepository {

    private final UserProfileReactiveCrudRepository userProfileRepository;

    public R2dbcUserProfileRepository(UserProfileReactiveCrudRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public Mono<UserProfile> findByUserId(UUID userId) {
        return userProfileRepository.findByUserId(userId)
                .map(this::toDomain);
    }

    @Override
    public Mono<UserProfile> save(UserProfile userProfile) {
        return userProfileRepository.upsert(
                        userProfile.userId(),
                        userProfile.avatarUrl(),
                        userProfile.displayName(),
                        userProfile.bio()
                )
                .map(this::toDomain);
    }

    private UserProfile toDomain(UserProfileDbo userProfileDbo) {
        return new UserProfile(
                userProfileDbo.getUserId(),
                userProfileDbo.getAvatarUrl(),
                userProfileDbo.getDisplayName(),
                userProfileDbo.getBio()
        );
    }

    private UserProfileDbo toDbo(UserProfile userProfile) {
        return new UserProfileDbo(
                userProfile.userId(),
                userProfile.avatarUrl(),
                userProfile.displayName(),
                userProfile.bio()
        );
    }
}


