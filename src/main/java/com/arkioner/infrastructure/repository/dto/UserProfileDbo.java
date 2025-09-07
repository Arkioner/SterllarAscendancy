package com.arkioner.infrastructure.repository.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("user_profile")
public class UserProfileDbo {
    @Id
    private UUID userId;
    private String avatarUrl;
    private String displayName;
    private String bio;

    public UserProfileDbo(UUID userId, String avatarUrl, String displayName, String bio) {
        this.userId = userId;
        this.avatarUrl = avatarUrl;
        this.displayName = displayName;
        this.bio = bio;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBio() {
        return bio;
    }
}

