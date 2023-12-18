package com.xlr8code.server.authentication.dto.revoke;

import jakarta.validation.constraints.NotBlank;

public record RevokeTokenRequestDTO(
        @NotBlank
        String token
) {
}
