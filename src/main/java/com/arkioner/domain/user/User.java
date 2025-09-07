package com.arkioner.domain.user;

import java.util.List;
import java.util.UUID;

public record User(
        UUID id,
        String username,
        String passwordHash,
        String avatarUrl,
        List<String> roles
) {}
