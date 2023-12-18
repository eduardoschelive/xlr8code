package com.xlr8code.server.common.exception;

import com.xlr8code.server.common.dto.ApplicationExceptionResponseDTO;
import com.xlr8code.server.common.dto.InvalidRequestContentResponseDTO;
import com.xlr8code.server.common.service.LocaleService;
import com.xlr8code.server.common.utils.StringUtils;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final LocaleService localeService;
    private final HttpServletRequest httpServletRequest;

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApplicationExceptionResponseDTO> handleApplicationError(ApplicationException applicationError) {
        var httpStatus = applicationError.getHttpStatus();
        var placeholders = applicationError.getPlaceholders();
        var messageIdentifier = applicationError.getMessageIdentifier();
        var errorMessage = applicationError.getMessage();

        var message = localeService.getMessage(messageIdentifier, httpServletRequest);

        if (placeholders != null) {
            for (int i = 0; i < placeholders.length; i++) {
                message = message.replace("{" + i + "}", String.valueOf(placeholders[i]));
            }
        }

        var applicationErrorResponse = new ApplicationExceptionResponseDTO(
                httpStatus.value(),
                errorMessage,
                message,
                new Date()
        );

        return ResponseEntity.status(httpStatus).body(applicationErrorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  @Nonnull HttpHeaders headers,
                                                                  @Nonnull HttpStatusCode status,
                                                                  @Nonnull WebRequest request) {
        var fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        this::getMessageForError,
                        (existing, replacement) -> existing
                ));

        return ResponseEntity.status(status).headers(headers).body(new InvalidRequestContentResponseDTO(
                status.value(),
                "INVALID_REQUEST_CONTENT",
                localeService.getMessage("validation.error.invalid_request_content", httpServletRequest),
                new Date(),
                fieldErrors
        ));
    }

    private String getMessageForError(FieldError error) {
        var constraintName = StringUtils.splitPascalCase(Objects.requireNonNull(error.getCode()));
        var messageKey = "validation.error." + constraintName.replace(" ", "_").toLowerCase();
        return localeService.getMessage(messageKey, httpServletRequest);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(@Nonnull Exception ex, Object body, @Nonnull HttpHeaders headers, HttpStatusCode statusCode, @Nonnull WebRequest request) {
        var response = new ApplicationExceptionResponseDTO(
                statusCode.value(),
                "INTERNAL_SERVER_ERROR",
                localeService.getMessage("error.internal_server_error", httpServletRequest),
                new Date()
        );

        return ResponseEntity.status(statusCode).headers(headers).body(response);
    }
}
