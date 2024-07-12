package com.xlr8code.server.swagger.customizer;

import com.xlr8code.server.common.exception.ApplicationException;
import com.xlr8code.server.common.helper.ApplicationExceptionHelper;
import com.xlr8code.server.swagger.annotation.ErrorResponse;
import com.xlr8code.server.swagger.utils.SwaggerUtils;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ExampleObjectCustomizer implements OperationCustomizer {

    private final ApplicationExceptionHelper applicationExceptionHelper;

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        var annotation = handlerMethod.getMethodAnnotation(ErrorResponse.class);
        if (annotation != null) {
            var exceptions = annotation.value();
            var exceptionsGroupByStatusCode = Arrays.stream(exceptions)
                    .map(SwaggerUtils::getExceptionMock)
                    .collect(Collectors.groupingBy(ApplicationException::getHttpStatus));
            exceptionsGroupByStatusCode.forEach((httpStatus, applicationExceptions) -> {
                var apiResponse = createApiResponse(httpStatus, applicationExceptions);
                operation.getResponses().addApiResponse(String.valueOf(httpStatus.value()), apiResponse);
            });
        }
        return operation;
    }

    private ApiResponse createApiResponse(HttpStatus httpStatus, List<ApplicationException> applicationExceptions) {
        var content = createContent(applicationExceptions);
        return new ApiResponse().description(httpStatus.getReasonPhrase()).content(content);
    }

    private Content createContent(List<ApplicationException> applicationExceptions) {
        var mediaType = createMediaType(applicationExceptions);
        return new Content().addMediaType("application/json", mediaType);
    }

    private MediaType createMediaType(List<ApplicationException> applicationExceptions) {
        var mediaType = new MediaType();
        applicationExceptions.forEach(applicationException ->
                mediaType.addExamples(applicationException.getMessage(), createExample(applicationException))
        );
        return mediaType;
    }

    private Example createExample(ApplicationException exception) {
        var exampleObject = applicationExceptionHelper.buildApplicationResponseFromApplicationException(exception);
        return new Example().value(exampleObject);
    }
}