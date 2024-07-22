package com.xlr8code.server.openapi.customizer;

import com.xlr8code.server.openapi.annotation.SecuredEndpoint;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import static com.xlr8code.server.authentication.utils.SessionCookieUtils.SESSION_TOKEN_COOKIE_NAME;

@Component
public class SecuredEndpointCustomizer implements OperationCustomizer {
    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {

        if (handlerMethod.hasMethodAnnotation(SecuredEndpoint.class)) {
            var annotation = handlerMethod.getMethodAnnotation(SecuredEndpoint.class);

            operation.addSecurityItem(new SecurityRequirement().addList(SESSION_TOKEN_COOKIE_NAME ,annotation.value().name()));
        }

        return operation;
    }
}
