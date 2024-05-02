package com.xlr8code.server.search.model;

public record SearchParam(
        SearchOperation operation,
        SearchSortType sortType,
        String key,
        String value
) {
}
