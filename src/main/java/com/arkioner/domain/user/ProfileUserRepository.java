package com.arkioner.domain.user;

import com.arkioner.domain.loginuser.LoginUserId;
import com.arkioner.domain.user.entity.ProfileUser;
import com.arkioner.domain.user.entity.RegisterProfileUserCommand;
import reactor.core.publisher.Mono;

public interface ProfileUserRepository {
    Mono<ProfileUser> findBy(LoginUserId id);
    Mono<ProfileUser> save(RegisterProfileUserCommand registerProfileUserCommand);
}

