package com.xlr8code.server.authentication.dto;

import java.util.Set;
import java.util.UUID;

public record SignUpResponseDTO(
        UUID uuid,
        String username,
        String email,
        boolean active,
        Set<String> roles,
        String languagePreference,
        String themePreference,
        String profilePictureUrl
) {
}
