package com.xlr8code.server.user.dto;

import com.xlr8code.server.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Schema(name = "User")
public record UserDTO(
        @Schema(description = "The unique identifier of the user")
        UUID id,
        @Schema(description = "The username of the user")
        String username,
        @Schema(description = "The email of the user")
        String email,
        @Schema(description = "If the user is currently active. The user is inactive if the email is not verified")
        boolean active,
        @Schema(description = "The date and time the user was created")
        Instant createdAt,
        @Schema(description = "The date and time the user was last updated")
        Instant updatedAt,
        @Schema(description = "The roles of the user")
        Set<String> roles,
        @Schema(description = "The metadata of the user")
        UserMetadataDTO metadata,
        @Schema(description = "The preferences of the user")
        UserPreferencesDTO preferences
) {

    public static UserDTO from(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.isActive(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getNamedRoles(),
                UserMetadataDTO.from(user.getMetadata()),
                UserPreferencesDTO.from(user.getPreferences()
                )
        );
    }
}
