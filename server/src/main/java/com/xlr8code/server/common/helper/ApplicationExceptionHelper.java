package com.xlr8code.server.common.helper;

import com.xlr8code.server.common.dto.ApplicationExceptionResponseDTO;
import com.xlr8code.server.common.dto.ApplicationFieldExceptionDTO;
import com.xlr8code.server.common.exception.ApplicationException;
import com.xlr8code.server.common.service.LocaleService;
import com.xlr8code.server.common.utils.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ApplicationExceptionHelper {

    private final LocaleService localeService;
    private final HttpServletRequest httpServletRequest;

    /**
     * @param applicationException The application exception to be converted to a response
     * @return The response to be sent to the client
     */
    public ApplicationExceptionResponseDTO buildApplicationResponseFromApplicationException(ApplicationException applicationException) {
        var httpStatus = applicationException.getHttpStatus();
        var placeholders = applicationException.getPlaceholders();
        var messageIdentifier = applicationException.getMessageIdentifier();
        var errorMessage = applicationException.getMessage();

        return this.buildApplicationExceptionResponseDTO(httpStatus, messageIdentifier, errorMessage, placeholders);
    }

    /**
     * @param httpStatus        The http status to be sent to the client
     * @param messageIdentifier The message identifier to be used to retrieve the message from the message source
     * @param errorMessage      The error message to be sent to the client
     * @param placeholders      The placeholders to be used in the message
     * @return {@link ApplicationExceptionResponseDTO} to be sent to the client
     */
    public ApplicationExceptionResponseDTO buildApplicationExceptionResponseDTO(HttpStatus httpStatus, String messageIdentifier, String errorMessage, Object... placeholders) {
        var message = this.localeService.getMessage(messageIdentifier, httpServletRequest);

        if (placeholders != null) {
            for (int i = 0; i < placeholders.length; i++) {
                message = message.replace("{" + i + "}", String.valueOf(placeholders[i]));
            }
        }

        return new ApplicationExceptionResponseDTO(
                httpStatus.value(),
                errorMessage,
                message,
                Instant.now(),
                httpServletRequest.getRequestURI()
        );

    }

    /**
     * @param messageIdentifier The message identifier to be used to retrieve the message from the message source
     * @param errorMessage      The error message to be sent to the client
     * @param fieldErrors       The field errors to be sent to the client
     * @return {@link ApplicationFieldExceptionDTO} to be sent to the client
     */
    public ApplicationFieldExceptionDTO buildApplicationFieldExceptionDTO(HttpStatus httpStatus, String messageIdentifier, String errorMessage, List<FieldError> fieldErrors) {
        var message = this.localeService.getMessage(messageIdentifier, httpServletRequest);

        var fields = fieldErrors.stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        this::getValidationMessageForField,
                        (existing, replacement) -> existing
                ));

        return new ApplicationFieldExceptionDTO(
                httpStatus.value(),
                errorMessage,
                message,
                Instant.now(),
                httpServletRequest.getRequestURI(),
                fields
        );
    }

    /**
     * @param error The field error to be analyzed
     * @return The message to be sent to the client for the given field error
     */
    public String getValidationMessageForField(FieldError error) {
        var constraintName = StringUtils.splitPascalCase(Objects.requireNonNull(error.getCode()));
        var messageKey = "validation.error." + constraintName.replace(" ", "_").toLowerCase();
        return localeService.getMessage(messageKey, httpServletRequest);
    }

    /**
     * @param httpStatus        The http status to be sent to the client
     * @param messageIdentifier The message identifier to be used to retrieve the message from the message source
     * @param errorMessage      The error message to be sent to the client
     * @param placeholders      The placeholders to be used in the message
     * @return {@link ResponseEntity} to be sent to the client
     */
    public ResponseEntity<Object> buildApplicationExceptionResponseEntity(HttpStatus httpStatus, HttpHeaders headers, String messageIdentifier, String errorMessage, Object... placeholders) {
        var applicationExceptionResponseDTO = this.buildApplicationExceptionResponseDTO(httpStatus, messageIdentifier, errorMessage, placeholders);
        return this.buildResponseEntityForException(httpStatus, headers, applicationExceptionResponseDTO);
    }


    /**
     * @param httpStatus        The http status to be sent to the client
     * @param headers           The headers to be sent to the client
     * @param messageIdentifier The message identifier to be used to retrieve the message from the message source
     * @param errorMessage      The error message to be sent to the client
     * @param fieldErrors       The field errors to be sent to the client
     * @return {@link ResponseEntity} to be sent to the client
     */
    public ResponseEntity<Object> buildApplicationFieldExceptionResponseEntity(HttpStatus httpStatus, HttpHeaders headers, String messageIdentifier, String errorMessage, List<FieldError> fieldErrors) {
        var applicationFieldExceptionDTO = this.buildApplicationFieldExceptionDTO(httpStatus, messageIdentifier, errorMessage, fieldErrors);
        return this.buildResponseEntityForException(httpStatus, headers, applicationFieldExceptionDTO);
    }

    /**
     * @param httpStatus The http status to be sent to the client
     * @param headers    The headers to be sent to the client
     * @param body       The body to be sent to the client
     * @return {@link ResponseEntity} to be sent to the client
     */
    public ResponseEntity<Object> buildResponseEntityForException(HttpStatus httpStatus, HttpHeaders headers, Object body) {
        return ResponseEntity.status(httpStatus).headers(headers).body(body);
    }

    /**
     * @param throwable The throwable to be analyzed
     * @return The first application exception found in the throwable chain or null if none is found
     */
    public ApplicationException getFirstApplicationExceptionInChain(Throwable throwable) {
        if (throwable == null) {
            return null;
        }

        if (throwable instanceof ApplicationException applicationException) {
            return applicationException;
        }

        return getFirstApplicationExceptionInChain(throwable.getCause());
    }

}
