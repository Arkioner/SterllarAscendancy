package com.arkioner.infrastructure.repository;

import com.arkioner.domain.loginuser.LoginUserId;
import com.arkioner.domain.user.ProfileUserRepository;
import com.arkioner.domain.user.entity.ProfileUser;
import com.arkioner.domain.user.entity.RegisterProfileUserCommand;
import com.arkioner.infrastructure.repository.dto.ProfileUserDbo;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;


@Repository
public class R2DbcProfileUserRepository implements ProfileUserRepository {

    private final ProfileUserReactiveCrudRepository profileUserRepository;

    public R2DbcProfileUserRepository(ProfileUserReactiveCrudRepository profileUserRepository) {
        this.profileUserRepository = profileUserRepository;
    }

    private ProfileUser toDomain(ProfileUserDbo profileUserDbo) {
        return new ProfileUser(
                new LoginUserId(profileUserDbo.getLoginUserId()),
                profileUserDbo.getAvatarUrl(),
                profileUserDbo.getDisplayName()
        );
    }

    private ProfileUserDbo toDbo(RegisterProfileUserCommand registerProfileUserCommand) {
        return new ProfileUserDbo(
                registerProfileUserCommand.loginUserId().value(),
                registerProfileUserCommand.avatarUrl(),
                registerProfileUserCommand.displayName()
        );
    }

    @Override
    public Mono<ProfileUser> findBy(LoginUserId id) {
        return profileUserRepository.findByLoginUserId(id.value())
                .map(this::toDomain);
    }

    @Override
    public Mono<ProfileUser> save(RegisterProfileUserCommand registerProfileUserCommand) {
        return Mono.just(registerProfileUserCommand)
                .map(this::toDbo)
                .flatMap(profileUserRepository::upsert)
                .map(this::toDomain);
    }
}


