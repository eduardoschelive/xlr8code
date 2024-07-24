package com.xlr8code.server.openapi.helper;

import com.xlr8code.server.common.dto.ApplicationExceptionResponseDTO;
import com.xlr8code.server.common.exception.ApplicationException;
import com.xlr8code.server.common.helper.ApplicationExceptionHelper;
import com.xlr8code.server.openapi.utils.OpenAPIUtils;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OpenAPIExceptionHelper {

    private final ApplicationExceptionHelper applicationExceptionHelper;

    /**
     * @param exceptionClasses List of exception classes to group by HttpStatus
     * @return Map of HttpStatus to List of exceptions
     */
    public Map<HttpStatus, List<ApplicationException>> groupExceptionsByStatusCode(List<Class<? extends ApplicationException>> exceptionClasses) {
        return exceptionClasses.stream()
                .map(OpenAPIUtils::getExceptionMock)
                .collect(Collectors.groupingBy(ApplicationException::getHttpStatus));
    }

    /**
     * @param operation             Operation to add the exception response to
     * @param httpStatus            HttpStatus of the exception
     * @param applicationExceptions List of exceptions to add to the operation as a response
     */
    public void addExceptionResponse(Operation operation, HttpStatus httpStatus, List<ApplicationException> applicationExceptions) {
        var resolvedSchema = ModelConverters.getInstance()
                .resolveAsResolvedSchema(new AnnotatedType(ApplicationExceptionResponseDTO.class));

        var apiResponse = new ApiResponse()
                .description(httpStatus.getReasonPhrase())
                .content(new Content().addMediaType("application/json", addExceptionMediaType(applicationExceptions).schema(resolvedSchema.schema)));
        operation.getResponses().addApiResponse(String.valueOf(httpStatus.value()), apiResponse);
    }

    /**
     * @param applicationExceptions List of exceptions
     * @return MediaType with examples of the exceptions
     */
    private MediaType addExceptionMediaType(List<ApplicationException> applicationExceptions) {
        var mediaType = new MediaType();
        applicationExceptions.forEach(applicationException -> addExceptionExample(applicationException, mediaType));
        return mediaType;
    }

    /**
     * @param applicationException Exception to add to the MediaType
     * @param mediaType            MediaType to add the exception to as an example
     */
    private void addExceptionExample(ApplicationException applicationException, MediaType mediaType) {
        var applicationResponse = applicationExceptionHelper.buildApplicationResponseFromApplicationException(applicationException);

        var exceptionExample = new Example()
                .value(applicationResponse)
                .description(applicationException.getMessage());

        mediaType.addExamples(applicationException.getErrorCode(), exceptionExample);
    }

}
