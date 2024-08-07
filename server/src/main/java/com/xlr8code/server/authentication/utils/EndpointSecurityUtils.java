package com.xlr8code.server.authentication.utils;

import com.xlr8code.server.common.utils.Endpoint;
import com.xlr8code.server.user.utils.UserRole;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EndpointSecurityUtils {

    public static final Map<String, List<EndpointSecurityDetails>> ENDPOINT_SECURITY = Map.of(
            Endpoint.User.BASE_PATH + "/**", List.of(
                    new EndpointSecurityDetails(HttpMethod.DELETE, UserRole.MEMBER),
                    new EndpointSecurityDetails(HttpMethod.PUT, UserRole.MEMBER),
                    new EndpointSecurityDetails(HttpMethod.PATCH, UserRole.MEMBER),
                    new EndpointSecurityDetails(HttpMethod.GET, null),
                    new EndpointSecurityDetails(HttpMethod.POST, null),
                    new EndpointSecurityDetails(HttpMethod.HEAD, null)
            ),
            Endpoint.Categories.BASE_PATH + "/**", List.of(
                    new EndpointSecurityDetails(HttpMethod.POST, UserRole.ADMIN),
                    new EndpointSecurityDetails(HttpMethod.DELETE, UserRole.ADMIN),
                    new EndpointSecurityDetails(HttpMethod.PUT, UserRole.ADMIN),
                    new EndpointSecurityDetails(HttpMethod.GET, null),
                    new EndpointSecurityDetails(HttpMethod.HEAD, null)
            ),
            Endpoint.Article.BASE_PATH + "/**", List.of(
                    new EndpointSecurityDetails(HttpMethod.POST, UserRole.ADMIN),
                    new EndpointSecurityDetails(HttpMethod.DELETE, UserRole.ADMIN),
                    new EndpointSecurityDetails(HttpMethod.PUT, UserRole.ADMIN),
                    new EndpointSecurityDetails(HttpMethod.GET, null),
                    new EndpointSecurityDetails(HttpMethod.HEAD, null)
            ),
            Endpoint.Authentication.BASE_PATH + "/**", List.of(
                    new EndpointSecurityDetails(HttpMethod.POST, null)
            )
    );

    public static List<EndpointSecurityDetails> getSecurityDetailsStartingWith(String endpoint) {
        return ENDPOINT_SECURITY.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(endpoint))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(List.of());
    }

}
