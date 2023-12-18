package com.xlr8code.server.common.exception;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

public interface ExceptionType extends Serializable {

    HttpStatus getHttpStatus();

    String name();

    String getMessageIdentifier();

}
