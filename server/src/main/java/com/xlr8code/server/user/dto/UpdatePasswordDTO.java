package com.xlr8code.server.user.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdatePasswordDTO(
        @NotBlank
        String oldPassword,
        @NotBlank
        String newPassword,
        @NotBlank
        String newPasswordConfirmation
) {
}
