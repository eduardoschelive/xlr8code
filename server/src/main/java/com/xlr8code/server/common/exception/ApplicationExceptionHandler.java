package com.xlr8code.server.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ApplicationError.class)
    public ResponseEntity<ApplicationErrorResponse> handleApplicationError(ApplicationError applicationError) {
        var errorCode = applicationError.getErrorCode();

        var applicationErrorResponse = new ApplicationErrorResponse(
                errorCode.getInternalCode(),
                errorCode.getMessage(),
                new Date()
        );

        return ResponseEntity.status(errorCode.getHttpStatus()).body(applicationErrorResponse);
    }

}
