package com.xlr8code.server.common.exception;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

public interface ExceptionType extends Serializable {

    String SEPARATOR = ".errors.";

    String getPrefix();

    HttpStatus getHttpStatus();

    String name();

    default String getMessageIdentifier() {
        return this.getPrefix() + SEPARATOR + this.name().toLowerCase();
    }

}
