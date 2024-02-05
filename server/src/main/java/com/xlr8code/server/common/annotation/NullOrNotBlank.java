package com.xlr8code.server.common.annotation;

import com.xlr8code.server.common.validator.NullOrNotBlankValidator;
import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
        validatedBy = NullOrNotBlankValidator.class
)
public @interface NullOrNotBlank {

    String message() default "Field must be null or not blank";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};

}
