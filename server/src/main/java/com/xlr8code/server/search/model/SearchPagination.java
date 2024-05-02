package com.xlr8code.server.search.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum SearchPagination {

    PAGE("_page", false),
    SIZE("_size", false),
    SORT("_sort", true);

    private final String suffix;
    private final boolean fieldDependant;

    private static final Map<String, SearchPagination> SUFFIX_ENUM =
            Arrays.stream(SearchPagination.values())
                    .collect(Collectors.toMap(SearchPagination::getSuffix, Function.identity()));

    /**
     * @param suffix the suffix to search for
     * @return the SearchPagination with the given suffix
     */
    public static SearchPagination fromSuffix(String suffix) {
        return SUFFIX_ENUM.get(suffix.toLowerCase());
    }

    /**
     * @param suffix the suffix to search for
     * @return true if the suffix is supported, false otherwise
     */
    public static boolean isSupported(String suffix) {
        return SUFFIX_ENUM.containsKey(suffix.toLowerCase());
    }

}
