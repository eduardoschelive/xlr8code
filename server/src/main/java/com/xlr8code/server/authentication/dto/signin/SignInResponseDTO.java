package com.xlr8code.server.authentication.dto.signin;

import java.util.UUID;

public record SignInResponseDTO(
        String token,
        UUID refreshToken) {
}
