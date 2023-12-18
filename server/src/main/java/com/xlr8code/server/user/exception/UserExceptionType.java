package com.xlr8code.server.user.exception;

import com.xlr8code.server.common.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum UserExceptionType implements ExceptionType {

    USER_ALREADY_EXISTS(HttpStatus.CONFLICT);

    private final HttpStatus httpStatus;
    private final String prefix = "user";

}
