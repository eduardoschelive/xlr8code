package com.xlr8code.server.common.exception;

import java.io.Serializable;

public interface ErrorCode extends Serializable {
    int getCode();
    String getMessage();
}
