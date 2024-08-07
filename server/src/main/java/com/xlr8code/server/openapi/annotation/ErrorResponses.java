package com.xlr8code.server.openapi.annotation;

import com.xlr8code.server.common.exception.ApplicationException;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ErrorResponses {

    Class<? extends ApplicationException>[] value() default ApplicationException.class;

}
