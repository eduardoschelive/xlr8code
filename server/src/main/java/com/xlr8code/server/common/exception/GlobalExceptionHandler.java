package com.xlr8code.server.common.exception;

import com.xlr8code.server.common.helper.ApplicationExceptionHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
@RequiredArgsConstructor
@Log4j2
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String INVALID_REQUEST_CONTENT = "INVALID_REQUEST_CONTENT";

    private final ApplicationExceptionHelper applicationExceptionHelper;

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<Object> handleApplicationError(ApplicationException applicationError) {
        var httpStatus = applicationError.getHttpStatus();
        var responseBody = this.applicationExceptionHelper.buildApplicationResponseFromApplicationException(applicationError);

        return ResponseEntity.status(httpStatus).body(responseBody);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var applicationException = this.applicationExceptionHelper.getFirstApplicationExceptionInChain(ex);
        if (applicationException != null) {
            return this.handleApplicationError(applicationException);
        }

        var httpStatus = HttpStatus.BAD_REQUEST;
        var messageIdentifier = "error.invalid_request_content";

        return this.applicationExceptionHelper.buildApplicationExceptionResponseEntity(httpStatus, headers, messageIdentifier, INVALID_REQUEST_CONTENT);
    }

    @ExceptionHandler(MissingRequestCookieException.class)
    public ResponseEntity<Object> handleMissingRequestCookieException(MissingRequestCookieException ex) {
        var httpStatus = HttpStatus.BAD_REQUEST;
        var messageIdentifier = "error.cookie_not_present";
        var errorCode = "COOKIE_NOT_PRESENT";
        var cookieName = ex.getCookieName();

        return this.applicationExceptionHelper.buildApplicationExceptionResponseEntity(httpStatus, null, messageIdentifier, errorCode, cookieName);
    }

    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var httpStatus = HttpStatus.NOT_FOUND;
        var messageIdentifier = "error.resource_not_found";
        var errorCode = "RESOURCE_NOT_FOUND";

        return this.applicationExceptionHelper.buildApplicationExceptionResponseEntity(httpStatus, headers, messageIdentifier, errorCode);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {

        return this.applicationExceptionHelper.buildApplicationFieldExceptionResponseEntity(
                HttpStatus.BAD_REQUEST,
                headers,
                "error.invalid_request_content",
                INVALID_REQUEST_CONTENT,
                ex.getBindingResult().getFieldErrors()
        );
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR) {
            log.error("Internal server error", ex);

            return this.applicationExceptionHelper.buildApplicationExceptionResponseEntity(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    headers,
                    "error.internal_server_error",
                    "INTERNAL_SERVER_ERROR"
            );
        }
        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    protected ResponseEntity<Object> handleInsufficientAuthenticationException() {
        var httpStatus = HttpStatus.FORBIDDEN;
        var messageIdentifier = "authentication.error.unauthorized";
        var errorCode = "UNAUTHORIZED";

        return this.applicationExceptionHelper.buildApplicationExceptionResponseEntity(httpStatus, null, messageIdentifier, errorCode);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return this.applicationExceptionHelper.buildApplicationExceptionResponseEntity(
                HttpStatus.BAD_REQUEST,
                headers,
                "error.missing_request_parameter",
                INVALID_REQUEST_CONTENT,
                ex.getParameterName()
        );
    }
}
