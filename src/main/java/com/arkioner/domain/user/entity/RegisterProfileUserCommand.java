package com.arkioner.domain.user.entity;

import com.arkioner.domain.loginuser.LoginUserId;

public record RegisterProfileUserCommand(
        LoginUserId loginUserId,
        String displayName,
        String avatarUrl
) {
    //TBD constrains on the fields
}
