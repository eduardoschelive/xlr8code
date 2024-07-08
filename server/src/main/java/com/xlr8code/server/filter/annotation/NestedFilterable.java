package com.xlr8code.server.filter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  Annotation to mark a field as nested filterable.
 *  <p>
 *      This annotation is used to mark a field as nested filterable. This means that all the fields of the nested object
 *      can be used as filters in a query. The annotation can be used on fields of any type.
 *      The annotation can be used on fields of any type.
 *  </p>
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NestedFilterable {
}
