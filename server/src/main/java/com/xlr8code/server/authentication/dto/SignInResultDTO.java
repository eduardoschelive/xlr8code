package com.xlr8code.server.authentication.dto;

import com.xlr8code.server.authentication.entity.UserSession;

public record SignInResultDTO(
        String token,
        UserSession userSession
) {
}
