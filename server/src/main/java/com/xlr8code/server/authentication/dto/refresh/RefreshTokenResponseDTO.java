package com.xlr8code.server.authentication.dto.refresh;

public record RefreshTokenResponseDTO(
        String token,
        String refreshToken
) {
}
