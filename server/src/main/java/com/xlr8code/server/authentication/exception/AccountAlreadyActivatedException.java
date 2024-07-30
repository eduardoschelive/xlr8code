package com.xlr8code.server.authentication.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class AccountAlreadyActivatedException extends ApplicationException {

    public AccountAlreadyActivatedException() {
        super("The account is already activated and cannot be activated again");
    }

    @Override
    public String getMessageIdentifier() {
        return "authentication.error.account_already_active";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getErrorCode() {
        return "ACCOUNT_ALREADY_ACTIVATED";
    }


}