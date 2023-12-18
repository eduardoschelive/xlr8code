package com.xlr8code.server.user.exception;

import com.xlr8code.server.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum UserErrorCode implements ErrorCode {

    USER_ALREADY_EXISTS(HttpStatus.CONFLICT);

    private final HttpStatus httpStatus;
    private final String prefix = "user";

}
