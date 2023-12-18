package com.xlr8code.server.authentication.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class AccountAlreadyActivatedException extends ApplicationException {

    public AccountAlreadyActivatedException() {
        super("ACCOUNT_ALREADY_ACTIVATED");
    }

    @Override
    public String getMessageIdentifier() {
        return "authentication.error.account_already_activated";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

}