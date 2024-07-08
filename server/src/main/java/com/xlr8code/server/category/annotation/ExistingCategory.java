package com.xlr8code.server.category.annotation;


import com.xlr8code.server.category.validator.ExistingCategoryValidator;
import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
        validatedBy = ExistingCategoryValidator.class
)
public @interface ExistingCategory {

    String message() default "Please provide a existing category id";

    boolean optional() default false;

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};

}
