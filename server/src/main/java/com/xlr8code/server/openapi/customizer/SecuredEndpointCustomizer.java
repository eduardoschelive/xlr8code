package com.xlr8code.server.openapi.customizer;

import com.xlr8code.server.authentication.utils.EndpointSecurityDetails;
import com.xlr8code.server.authentication.utils.EndpointSecurityUtils;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;

import static com.xlr8code.server.authentication.utils.SessionCookieUtils.SESSION_TOKEN_COOKIE_NAME;

@Component
public class SecuredEndpointCustomizer implements OperationCustomizer {

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        if (!isAnnotatedWithRequestMapping(handlerMethod)) {
            return operation;
        }

        HttpMethod httpMethod = getHttpMethod(handlerMethod);
        if (httpMethod == null) {
            return operation;
        }

        String fullEndpoint = getFullEndpoint(handlerMethod);
        EndpointSecurityUtils.getSecurityDetailsStartingWith(fullEndpoint).stream()
                .filter(details -> details.method().equals(httpMethod))
                .findFirst()
                .map(EndpointSecurityDetails::minimumRole)
                .ifPresent(roleForMethod ->
                        operation.addSecurityItem(new SecurityRequirement().addList(SESSION_TOKEN_COOKIE_NAME, roleForMethod.name())));

        return operation;
    }

    private HttpMethod getHttpMethod(HandlerMethod handlerMethod) {
        RequestMapping requestMapping = handlerMethod.getMethodAnnotation(RequestMapping.class);
        if (requestMapping == null || requestMapping.method().length == 0) {
            return null;
        }
        return requestMapping.method()[0].asHttpMethod();
    }

    private String getFullEndpoint(HandlerMethod handlerMethod) {
        String controllerPath = getPath(handlerMethod.getBeanType().getAnnotation(RequestMapping.class));
        String methodPath = getPath(handlerMethod.getMethodAnnotation(RequestMapping.class));
        return controllerPath + methodPath;
    }

    private String getPath(RequestMapping requestMapping) {
        if (requestMapping == null || requestMapping.value().length == 0) {
            return "";
        }
        String path = requestMapping.value()[0];
        return isVariable(path) ? "" : path;
    }

    private boolean isVariable(String path) {
        return path.contains("{") && path.contains("}");
    }

    private boolean isAnnotatedWithRequestMapping(HandlerMethod handlerMethod) {
        return handlerMethod.hasMethodAnnotation(RequestMapping.class) ||
                handlerMethod.getBeanType().isAnnotationPresent(RequestMapping.class);
    }
}
