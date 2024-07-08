package com.xlr8code.server.article.annotation;


import com.xlr8code.server.article.validator.ExistingArticleValidator;
import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
        validatedBy = ExistingArticleValidator.class
)
public @interface ExistingArticle {

    String message() default "Please provide a existing article id";

    boolean optional() default false;

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};

}
