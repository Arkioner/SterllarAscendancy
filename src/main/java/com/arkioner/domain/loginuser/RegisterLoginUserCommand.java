package com.arkioner.domain.loginuser;

public record RegisterLoginUserCommand(
        LoginUserId id,
        String username,
        String passwordHash
) {
// TBD validations
}
