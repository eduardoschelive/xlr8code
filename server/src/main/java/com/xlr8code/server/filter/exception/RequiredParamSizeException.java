package com.xlr8code.server.filter.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import com.xlr8code.server.filter.utils.FilterConstants;
import org.springframework.http.HttpStatus;

import static com.xlr8code.server.filter.utils.FilterConstants.FILTER_VALUE_SEPARATOR;

public class RequiredParamSizeException extends ApplicationException {

    public RequiredParamSizeException(String operation, int requiredParamSize) {
        super("The operation " + operation + " requires " + requiredParamSize + " parameters", operation, requiredParamSize, FILTER_VALUE_SEPARATOR);
    }

    @Override
    public String getMessageIdentifier() {
        return "filter.error.required_param_size";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getErrorCode() {
        return "REQUIRED_PARAM_SIZE";
    }

}
