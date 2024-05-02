package com.xlr8code.server.search.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum SearchSortType {

    ASC("asc"),
    DESC("desc");

    private final String suffix;

    private static final Map<String, SearchSortType> SUFFIX_ENUM =
            Arrays.stream(SearchSortType.values())
                    .collect(Collectors.toMap(SearchSortType::getSuffix, Function.identity()));

    public static SearchSortType fromSuffix(String suffix) {
        return SUFFIX_ENUM.get(suffix.toLowerCase());
    }

}
