package com.arkioner.infrastructure.repository.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("profile_user")
public class ProfileUserDbo {
    @Id
    private UUID loginUserId;
    private String avatarUrl;
    private String displayName;

    public ProfileUserDbo(UUID loginUserId, String avatarUrl, String displayName) {
        this.loginUserId = loginUserId;
        this.avatarUrl = avatarUrl;
        this.displayName = displayName;
    }

    public UUID getLoginUserId() {
        return loginUserId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getDisplayName() {
        return displayName;
    }
}

