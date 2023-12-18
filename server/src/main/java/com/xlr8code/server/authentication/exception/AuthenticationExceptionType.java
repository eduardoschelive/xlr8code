package com.xlr8code.server.authentication.exception;

import com.xlr8code.server.common.exception.ExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum AuthenticationExceptionType implements ExceptionType {

    JWT_CREATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "authentication.error.jwt_creation_error"),
    JWT_VERIFICATION_ERROR(HttpStatus.UNAUTHORIZED, "authentication.error.jwt_verification_error"),
    INCORRECT_USERNAME_OR_PASSWORD(HttpStatus.UNAUTHORIZED, "authentication.error.incorrect_username_or_password"),
    ACCOUNT_NOT_ACTIVATED(HttpStatus.UNAUTHORIZED, "authentication.error.account_not_activated");

    private final HttpStatus httpStatus;
    private final String messageIdentifier;

}
