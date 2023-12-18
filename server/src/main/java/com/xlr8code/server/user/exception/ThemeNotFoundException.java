package com.xlr8code.server.user.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class ThemeNotFoundException extends ApplicationException {

    public ThemeNotFoundException(String themeCode) {
        super("THEME_NOT_FOUND", themeCode);
    }

    @Override
    public String getMessageIdentifier() {
        return "user.error.theme_not_found";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }

}
