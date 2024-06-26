package com.xlr8code.server.filter.utils;

public record FilterableFieldDetails(
        Class<?> fieldType,
        String fieldPath
) {
}
