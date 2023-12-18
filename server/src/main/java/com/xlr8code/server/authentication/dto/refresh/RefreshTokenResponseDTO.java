package com.xlr8code.server.authentication.dto.refresh;

import java.util.UUID;

public record RefreshTokenResponseDTO(
        String token,
        UUID refreshToken
) {
}
