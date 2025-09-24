package com.arkioner.domain.loginuser;

import java.util.UUID;

public record LoginUserId(UUID value) {
    public static LoginUserId generate() {
        return new LoginUserId(UUID.randomUUID());
    }
}
