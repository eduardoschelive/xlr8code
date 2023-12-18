package com.xlr8code.server.authentication.exception;

import com.xlr8code.server.common.exception.ExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum AuthenticationException implements ExceptionType {

    JWT_CREATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR),
    JWT_VERIFICATION_ERROR(HttpStatus.UNAUTHORIZED);

    private final HttpStatus httpStatus;
    private final String prefix = "authentication";

}
