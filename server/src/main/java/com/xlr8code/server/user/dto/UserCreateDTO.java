package com.xlr8code.server.user.dto;

import com.xlr8code.server.user.utils.UserRole;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record UserCreateDTO(
        @NotBlank
        String username,
        @NotBlank @Email
        String email,
        @NotBlank
        String password,
        @NotNull
        Set<UserRole> roles,
        @NotBlank
        String themePreference,
        @NotBlank
        String languagePreference,
        @Nullable
        String profilePictureUrl,
        @Nullable
        Boolean active
) {
}
