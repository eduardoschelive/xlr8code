package com.xlr8code.server.authentication.dto;

import com.xlr8code.server.authentication.annotation.UUIDToken;
import jakarta.validation.constraints.NotBlank;

public record RefreshSessionDTO(
        @NotBlank
        @UUIDToken
        String refreshSessionToken
) {
}
