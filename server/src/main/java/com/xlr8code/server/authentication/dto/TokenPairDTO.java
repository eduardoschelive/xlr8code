package com.xlr8code.server.authentication.dto;

import java.util.UUID;

public record TokenPairDTO(
        String token,
        UUID sessionToken
) {
}
