package com.xlr8code.server.authentication.dto.refresh;

import com.xlr8code.server.authentication.annotation.UUIDToken;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record RefreshTokenRequestDTO(
        @NotBlank @UUIDToken String refreshToken
) {
    public UUID refreshTokenAsUUID() {
        return UUID.fromString(this.refreshToken);
    }

}
