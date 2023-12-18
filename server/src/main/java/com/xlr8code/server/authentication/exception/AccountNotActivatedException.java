package com.xlr8code.server.authentication.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class AccountNotActivatedException extends ApplicationException {

    public AccountNotActivatedException() {
        super("ACCOUNT_NOT_ACTIVATED");
    }

    @Override
    public String getMessageIdentifier() {
        return "authentication.error.account_not_active";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }

}

