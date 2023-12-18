package com.xlr8code.server.authentication.dto.signup;

public record SignUpResponseDTO(
        String token,
        String refreshToken
) {
}
