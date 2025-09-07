package com.arkioner.infrastructure.web;

import com.arkioner.domain.user.UserProfile;
import com.arkioner.domain.user.UserProfileService;
import com.arkioner.infrastructure.security.SecurityConfig;
import com.arkioner.infrastructure.web.dto.UserProfileUpdateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@RestController
@RequestMapping("/api/profile")
public class UserProfileController {
    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping
    public Mono<UserProfile> getProfile(@AuthenticationPrincipal SecurityConfig.JwtAuthentication auth) {
        return userProfileService.getProfile(auth.getUserId());
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<UserProfile> updateProfile(
            @AuthenticationPrincipal SecurityConfig.JwtAuthentication auth,
            @RequestBody UserProfileUpdateRequest userProfileUpdateRequest
    ) {
        return Mono.justOrEmpty(userProfileUpdateRequest)
                .map(toUserProfile(auth))
                .flatMap(userProfileService::updateProfile);
    }

    private static Function<UserProfileUpdateRequest, UserProfile> toUserProfile(SecurityConfig.JwtAuthentication auth) {
        return (UserProfileUpdateRequest request) ->
                new UserProfile(
                        auth.getUserId(),
                        request.avatarUrl(),
                        request.displayName(),
                        request.bio()
                );
    }
}
