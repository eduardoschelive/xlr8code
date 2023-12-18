package com.xlr8code.server.authentication.dto;

public record RefreshTokenResponseDTO(
        String token,
        String refreshToken
) implements TokenResponse {
}
