package com.xlr8code.server.user.exception;

import com.xlr8code.server.common.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum UserExceptionType implements ExceptionType {

    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "user.error.user_already_exists"),
    THEME_NOT_FOUND(HttpStatus.NOT_FOUND, "user.error.theme_not_found"),
    LANGUAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "user.error.language_not_found");

    private final HttpStatus httpStatus;
    private final String messageIdentifier;

}
