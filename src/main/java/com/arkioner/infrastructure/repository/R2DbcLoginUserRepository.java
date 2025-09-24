package com.arkioner.infrastructure.repository;

import com.arkioner.domain.loginuser.LoginUser;
import com.arkioner.domain.loginuser.LoginUserId;
import com.arkioner.domain.loginuser.LoginUserRepository;
import com.arkioner.domain.loginuser.RegisterLoginUserCommand;
import io.r2dbc.spi.Readable;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public class R2DbcLoginUserRepository implements LoginUserRepository {
    private final DatabaseClient dbc;

    public R2DbcLoginUserRepository(DatabaseClient dbc) {
        this.dbc = dbc;
    }

    @Override
    public Mono<LoginUser> findBy(String username) {
        return dbc.sql("SELECT id, username, password_hash FROM login_user WHERE username = :username")
                .bind("username", username)
                .map(this::toUser)
                .one();
    }

    @Override
    public Mono<LoginUser> save(RegisterLoginUserCommand registerLoginUserCommand) {
        String sql = "INSERT INTO login_user (id, username, password_hash) VALUES (:id, :username, :password_hash) " +
                "ON CONFLICT (id) DO UPDATE SET password_hash = EXCLUDED.password_hash RETURNING id, username, password_hash";
        return dbc.sql(sql)
                .bind("id", registerLoginUserCommand.id().value())
                .bind("username", registerLoginUserCommand.username())
                .bind("password_hash", registerLoginUserCommand.passwordHash())
                .map(this::toUser)
                .one();
    }

    private LoginUser toUser(Readable row) {
        UUID id = row.get("id", UUID.class);
        String username = row.get("username", String.class);
        String pw = row.get("password_hash", String.class);
        return new LoginUser(new LoginUserId(id), username, pw);
    }
}
