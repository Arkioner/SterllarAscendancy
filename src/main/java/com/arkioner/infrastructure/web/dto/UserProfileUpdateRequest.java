package com.arkioner.infrastructure.web.dto;

public record UserProfileUpdateRequest (
        String avatarUrl,
        String displayName
) {
}
