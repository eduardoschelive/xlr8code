package com.xlr8code.server.authentication.exception;

import com.xlr8code.server.common.exception.ExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum AuthenticationException implements ExceptionType {

    JWT_CREATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "authentication.error.jwt_creation_error"),
    JWT_VERIFICATION_ERROR(HttpStatus.UNAUTHORIZED, "authentication.error.jwt_verification_error");

    private final HttpStatus httpStatus;
    private final String messageIdentifier;

}
