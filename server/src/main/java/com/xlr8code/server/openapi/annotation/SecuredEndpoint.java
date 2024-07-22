package com.xlr8code.server.openapi.annotation;

import com.xlr8code.server.common.exception.ApplicationException;
import com.xlr8code.server.user.utils.UserRole;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SecuredEndpoint {

    UserRole value();

}
