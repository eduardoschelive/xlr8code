package com.xlr8code.server.filter.utils;

public record FilterFieldDetails(
        Class<?> fieldType,
        String fieldPath
) {
}
