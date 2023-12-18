package com.xlr8code.server.common.exception;

import com.xlr8code.server.common.service.LocaleService;
import com.xlr8code.server.user.utils.StringUtils;
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
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final LocaleService localeService;
    private final HttpServletRequest httpServletRequest;

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApplicationExceptionResponse> handleApplicationError(ApplicationException applicationError) {
        var errorCode = applicationError.getErrorCode();
        var replacements = applicationError.getPlaceholders();

        var message = localeService.getMessage(errorCode.getMessageIdentifier(), httpServletRequest);

        if (replacements != null) {
            for (int i = 0; i < replacements.length; i++) {
                message = message.replace("{" + i + "}", String.valueOf(replacements[i]));
            }
        }

        var applicationErrorResponse = new ApplicationExceptionResponse(
                message,
                new Date()
        );

        return ResponseEntity.status(errorCode.getHttpStatus()).body(applicationErrorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  @Nonnull HttpHeaders headers,
                                                                  @Nonnull HttpStatusCode status,
                                                                  @Nonnull WebRequest request) {
        Map<String, String> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        this::getMessageForError,
                        (existing, replacement) -> existing
                ));

        return ResponseEntity.status(status).headers(headers).body(new InvalidRequestContentResponse(
                localeService.getMessage("validation.error.invalid-request-content", httpServletRequest),
                new Date(),
                fieldErrors
        ));
    }

    private String getMessageForError(FieldError error) {
        String constraintName = StringUtils.splitFromPascalCase(Objects.requireNonNull(error.getCode()));
        String messageKey = "validation.error." + constraintName.replace(" ", ".").toLowerCase();
        return localeService.getMessage(messageKey, httpServletRequest);
    }

}
