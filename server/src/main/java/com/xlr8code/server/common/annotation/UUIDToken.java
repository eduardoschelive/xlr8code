package com.xlr8code.server.common.annotation;

import com.xlr8code.server.common.validator.UUIDTokenValidator;
import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
        validatedBy = UUIDTokenValidator.class
)
public @interface UUIDToken {

    String message() default "Invalid UUID token";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};

}
