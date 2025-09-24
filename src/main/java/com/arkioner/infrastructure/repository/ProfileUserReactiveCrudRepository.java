package com.arkioner.infrastructure.repository;

import com.arkioner.infrastructure.repository.dto.ProfileUserDbo;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface ProfileUserReactiveCrudRepository extends ReactiveCrudRepository<ProfileUserDbo, UUID> {
    Mono<ProfileUserDbo> findByLoginUserId(UUID loginUserId);

    @Query("""
    INSERT INTO profile_user (login_user_id, avatar_url, display_name)
    VALUES (:#{#profile.userId}, :#{#profile.avatarUrl}, :#{#profile.displayName})
    ON CONFLICT (login_user_id) DO UPDATE SET
        avatar_url = EXCLUDED.avatar_url,
        display_name = EXCLUDED.display_name
    RETURNING *
    """)
    Mono<ProfileUserDbo> upsert(@Param("profile") ProfileUserDbo profileUserDbo);

}
