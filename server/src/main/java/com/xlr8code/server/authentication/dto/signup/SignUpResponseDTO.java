package com.xlr8code.server.authentication.dto.signup;

import java.util.UUID;

public record SignUpResponseDTO(
        String token,
        UUID refreshToken) {
}
