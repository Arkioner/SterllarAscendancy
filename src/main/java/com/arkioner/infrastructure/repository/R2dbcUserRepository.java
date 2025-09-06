package com.arkioner.infrastructure.repository;

import com.arkioner.domain.user.User;
import com.arkioner.domain.user.UserRepository;
import io.r2dbc.spi.Readable;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Repository
public class R2dbcUserRepository implements UserRepository {
    private final DatabaseClient db;

    public R2dbcUserRepository(DatabaseClient db) {
        this.db = db;
    }

    @Override
    public Mono<User> findByUsername(String username) {
        return db.sql("SELECT id, username, password_hash, avatar_url, roles FROM app_user WHERE username = :username")
                .bind("username", username)
                .map(this::toUser)
                .one();
    }

    @Override
    public Mono<User> findById(UUID id) {
        return db.sql("SELECT id, username, password_hash, avatar_url, roles FROM app_user WHERE id = :id")
                .bind("id", id)
                .map(this::toUser)
                .one();
    }

    @Override
    public Mono<User> save(User user) {
        String sql = "INSERT INTO app_user (id, username, password_hash, avatar_url, roles) VALUES (:id, :username, :password_hash, :avatar_url, :roles) " +
                "ON CONFLICT (username) DO UPDATE SET password_hash = EXCLUDED.password_hash, avatar_url = EXCLUDED.avatar_url, roles = EXCLUDED.roles RETURNING id, username, password_hash, avatar_url, roles";
        return db.sql(sql)
                .bind("id", user.id())
                .bind("username", user.username())
                .bind("password_hash", user.passwordHash())
                .bind("avatar_url", user.avatarUrl())
                .bind("roles", user.roles().toArray(new String[0]))
                .map(this::toUser)
                .one();
    }

    private User toUser(Readable row) {
        UUID id = row.get("id", UUID.class);
        String username = row.get("username", String.class);
        String pw = row.get("password_hash", String.class);
        String avatar = row.get("avatar_url", String.class);
        Object rolesObj = row.get("roles");
        List<String> roles;
        if (rolesObj instanceof String[]) {
            roles = Arrays.asList((String[]) rolesObj);
        } else {
            roles = List.of("USER");
        }
        return new User(id, username, pw, avatar, roles);
    }
}
