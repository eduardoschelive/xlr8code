package com.xlr8code.server.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CommonExceptionType implements ExceptionType {

    THEME_NOT_FOUND(HttpStatus.NOT_FOUND, "common.error.theme_not_found"),
    LANGUAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "common.error.language_not_found");

    private final HttpStatus httpStatus;
    private final String messageIdentifier;

}
