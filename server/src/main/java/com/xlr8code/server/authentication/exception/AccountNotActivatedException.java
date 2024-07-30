package com.xlr8code.server.authentication.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class AccountNotActivatedException extends ApplicationException {

    public AccountNotActivatedException() {
        super("The account is not activated and cannot be used for authentication");
    }

    @Override
    public String getMessageIdentifier() {
        return "authentication.error.account_not_active";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }

    @Override
    public String getErrorCode() {
        return "ACCOUNT_NOT_ACTIVATED";
    }

}

