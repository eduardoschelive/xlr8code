package com.xlr8code.server.search.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SearchPagination {

    PAGE("page"),
    SIZE("size");

    private final String suffix;
}
