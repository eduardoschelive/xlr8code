package com.xlr8code.server.series.annotation;


import com.xlr8code.server.series.validator.ExistingSeriesValidator;
import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
        validatedBy = ExistingSeriesValidator.class
)
public @interface ExistingSeries {

        String message() default "Please provide a existing series id";

        boolean optional() default false;

        Class<?>[] groups() default {};

        Class<?>[] payload() default {};

}
