package com.xlr8code.server.common.utils;

import com.xlr8code.server.article.entity.ArticleRelation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.Optional;
import java.util.function.Function;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ObjectUtils {

    /**
     * Executes the provided function on the given value if the value is not null.
     * Returns null if the value is null.
     *
     * @param value    the value to execute the function on
     * @param function the function to execute
     * @param <T>      the type of the input value
     * @param <R>      the type of the result value
     * @return the result of executing the function on the value, or null if the value is null
     */
    public static <T, R> R executeIfNotNull(T value, Function<T, R> function) {
        return Optional.ofNullable(value)
                .map(function)
                .orElse(null);
    }

}
