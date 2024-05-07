package com.xlr8code.server.search.model;

public record FieldValue<V>(
        String field,
        V value
) {
}
