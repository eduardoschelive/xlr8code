package com.xlr8code.server.authentication.dto;

public record SignInResponseDTO(String token, String refreshToken) implements TokenResponse{
}
