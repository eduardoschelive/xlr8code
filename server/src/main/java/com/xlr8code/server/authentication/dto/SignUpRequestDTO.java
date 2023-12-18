package com.xlr8code.server.authentication.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignUpRequestDTO(
        @NotBlank
        String username,
        @NotBlank @Email
        String email,
        @NotBlank
        String password,
        @Nullable
        String themePreference,
        @Nullable
        String languagePreference,
        @Nullable
        String profilePictureUrl
) {
}