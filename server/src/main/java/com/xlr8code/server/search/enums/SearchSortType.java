package com.xlr8code.server.search.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SearchSortType {

    ASC("asc"),
    DESC("desc");

    private final String value;

}
