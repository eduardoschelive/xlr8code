package com.xlr8code.server.openapi.customizer;

import com.xlr8code.server.openapi.annotation.ErrorResponse;
import com.xlr8code.server.openapi.helper.OpenAPIExceptionHelper;
import io.swagger.v3.oas.models.Operation;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class RequestExceptionsCustomizer implements OperationCustomizer {

    private final OpenAPIExceptionHelper openAPIExceptionHelper;

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        var annotation = handlerMethod.getMethodAnnotation(ErrorResponse.class);
        if (annotation != null) {
            var exceptions = Arrays.stream(annotation.value()).toList();

            var groupedExceptions = openAPIExceptionHelper.groupExceptionsByStatusCode(exceptions);

            groupedExceptions.forEach((httpStatus, applicationExceptions) ->
                    openAPIExceptionHelper.addExceptionResponse(operation, httpStatus, applicationExceptions));

        }
        return operation;
    }
}