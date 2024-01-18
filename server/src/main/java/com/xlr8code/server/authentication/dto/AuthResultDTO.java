package com.xlr8code.server.authentication.dto;

public record AuthResultDTO(
        String token,
        String sessionToken
) {
}
