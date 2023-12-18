package com.xlr8code.server.authentication.dto;

import com.xlr8code.server.user.entity.User;

import java.util.Set;
import java.util.UUID;

public record SignUpResponseDTO(
        String token,
        String refreshToken
) implements TokenResponse{
}
