package com.xlr8code.server.user.dto;

import com.xlr8code.server.user.entity.User;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record UserDTO(
        UUID id,
        String username,
        String email,
        String themePreference,
        String languagePreference,
        String profilePictureUrl,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Set<String> roles
) {

    public static UserDTO fromUser(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getMetadata().getThemePreference().getCode(),
                user.getMetadata().getLanguagePreference().getCode(),
                user.getMetadata().getProfilePictureUrl(),
                user.isActive(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getNamedRoles()
        );
    }

}
