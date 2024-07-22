package com.xlr8code.server.authentication.utils;

import com.xlr8code.server.user.utils.UserRole;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;

public record EndpointSecurityDetails(
        HttpMethod method, @Nullable UserRole minimumRole
) {
}
