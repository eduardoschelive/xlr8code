package com.xlr8code.server.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateUserDTO(
        @NotBlank
        String username,
        @NotBlank @Email
        String email
) {
}
