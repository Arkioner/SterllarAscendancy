package com.arkioner.domain.loginuser;

import java.util.List;
import java.util.UUID;

public record LoginUser(
        LoginUserId id,
        String username,
        String passwordHash
) {

}
