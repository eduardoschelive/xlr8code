package com.xlr8code.server.swagger.annotation;

import com.xlr8code.server.common.exception.ApplicationException;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ErrorResponse {

    Class<? extends ApplicationException> exception() default ApplicationException.class;

}
