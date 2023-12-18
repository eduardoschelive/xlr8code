package com.xlr8code.server.common.exception;

import com.xlr8code.server.common.service.LocaleService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
@RequiredArgsConstructor
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {
    
    private final LocaleService localeService;
    private final HttpServletRequest httpServletRequest;

    @ExceptionHandler(ApplicationError.class)
    public ResponseEntity<ApplicationErrorResponse> handleApplicationError(ApplicationError applicationError) {
        var errorCode = applicationError.getErrorCode();

        var message = localeService.getMessage(errorCode.getMessageIdentifier(), httpServletRequest);

        var applicationErrorResponse = new ApplicationErrorResponse(
                message,
                new Date()
        );

        return ResponseEntity.status(errorCode.getHttpStatus()).body(applicationErrorResponse);
    }

}
