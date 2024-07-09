package com.xlr8code.server.filter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark a field as filterable.
 * <p>
 * This annotation is used to mark a field as filterable. This means that the field can be used as a filter in a
 * query. The annotation can be used to specify a custom path to the field in the query.
 * If no custom path is specified, the field name will be used as the path.
 * The annotation can be used on fields of any type.
 * </p>
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Filterable {
    String customPath() default "";
}
