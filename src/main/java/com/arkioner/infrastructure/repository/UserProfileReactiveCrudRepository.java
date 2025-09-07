package com.arkioner.infrastructure.repository;

import com.arkioner.domain.user.UserProfile;
import com.arkioner.infrastructure.repository.dto.UserProfileDbo;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface UserProfileReactiveCrudRepository extends ReactiveCrudRepository<UserProfileDbo, UUID> {
    Mono<UserProfileDbo> findByUserId(UUID userId);

    @Query("""
        INSERT INTO user_profile (user_id, avatar_url, display_name, bio)
        VALUES (:userId, :avatarUrl, :displayName, :bio)
        ON CONFLICT (user_id) DO UPDATE SET
            avatar_url = EXCLUDED.avatar_url,
            display_name = EXCLUDED.display_name,
            bio = EXCLUDED.bio,
            updated_at = now()
        RETURNING *
        """)
    Mono<UserProfileDbo> upsert(UUID userId, String avatarUrl, String displayName, String bio);
}
