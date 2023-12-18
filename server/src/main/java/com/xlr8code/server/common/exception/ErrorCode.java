package com.xlr8code.server.common.exception;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

public interface ErrorCode extends Serializable {
    HttpStatus getHttpStatus();

    int getInternalCode();

    String getMessage();
}
