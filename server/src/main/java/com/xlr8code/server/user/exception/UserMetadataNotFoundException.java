package com.xlr8code.server.user.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class UserMetadataNotFoundException extends ApplicationException {

    public UserMetadataNotFoundException() {
        super("The user metadata was not found for the specified user");
    }

    @Override
    public String getMessageIdentifier() {
        return "user.error.user_metadata_not_found";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public String getErrorCode() {
        return "USER_METADATA_NOT_FOUND";
    }

}
