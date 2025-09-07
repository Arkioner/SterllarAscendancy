package com.arkioner.domain.user;

import java.util.UUID;

public record UserProfile(
        UUID userId,
        String avatarUrl,
        String displayName,
        String bio
) {
}
