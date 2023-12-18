package com.xlr8code.server.user.exception;

import com.xlr8code.server.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum UserErrorCode implements ErrorCode {
    USER_ALREADY_EXISTS(1000, "User already exists");

    private final int code;
    private final String message;
}
