package com.arkioner.domain.user.entity;

import com.arkioner.domain.loginuser.LoginUserId;

public record ProfileUser(
        LoginUserId loginUserId,
        String displayName,
        String avatarUrl
) {
}
