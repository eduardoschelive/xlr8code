package com.xlr8code.server.authentication.dto;

import jakarta.validation.constraints.NotBlank;

public record ActivateUserDTO(
        @NotBlank
        String code
) {
}
