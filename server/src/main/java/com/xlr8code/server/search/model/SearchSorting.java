package com.xlr8code.server.search.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum SearchSorting {

    SORT("sort");

    private final String suffix;

    private static final Map<String, SearchSorting> SUFFIX_ENUM =
            Arrays.stream(SearchSorting.values())
                    .collect(Collectors.toMap(SearchSorting::getSuffix, Function.identity()));

    public static SearchSorting fromSuffix(String suffix) {
        return SUFFIX_ENUM.get(suffix.toLowerCase());
    }

    public static boolean isSupported(String suffix) {
        return SUFFIX_ENUM.containsKey(suffix.toLowerCase());
    }

}
