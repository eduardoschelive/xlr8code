package com.xlr8code.server.user.dto;

import com.xlr8code.server.user.entity.User;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record UserDTO(
        UUID id,
        String username,
        String email,
        boolean active,
        Instant createdAt,
        Instant updatedAt,
        Set<String> roles,
        UserMetadataDTO metadata
) {

    public static UserDTO fromUser(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.isActive(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getNamedRoles(),
                new UserMetadataDTO(
                        user.getMetadata().getThemePreference(),
                        user.getMetadata().getLanguagePreference(),
                        user.getMetadata().getProfilePictureUrl()
                )
        );
    }

}
