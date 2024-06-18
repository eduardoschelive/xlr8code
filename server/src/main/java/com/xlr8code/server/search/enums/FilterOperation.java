package com.xlr8code.server.search.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public enum FilterOperation {

    EQUALITY("eq", true),
    STARTS_WITH("sw", true),
    ENDS_WITH("ew", true),
    LIKE("lk", true),
    IN("in", true),

    GREATER_THAN("gt", false),
    GREATER_THAN_OR_EQUAL_TO("gte", false),
    LESS_THAN("lt", false),
    LESS_THAN_OR_EQUAL_TO("lte", false),
    NULL("null", false),
    EMPTY("empty", false),
    BETWEEN("btw", false);

    private final String suffix;
    private final boolean allowIgnoreCase;

    private static final Map<String, FilterOperation> OPERATIONS_MAP = Arrays.stream(FilterOperation.values())
            .collect(Collectors.toMap(FilterOperation::getSuffix, Function.identity()));

    public static FilterOperation fromSuffix(String suffix) {
        return OPERATIONS_MAP.get(suffix);
    }

    public static boolean isSupported(String suffix) {
        return OPERATIONS_MAP.containsKey(suffix);
    }

}
