package com.xlr8code.server.filter.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Enum representing the operations that can be performed on a field in a filter.
 */
@AllArgsConstructor
@Getter
public enum FilterOperation {

    EQUALITY("eq"),
    STARTS_WITH("sw"),
    ENDS_WITH("ew"),
    LIKE("lk"),
    IN("in"),

    GREATER_THAN("gt"),
    GREATER_THAN_OR_EQUAL_TO("gte"),
    LESS_THAN("lt"),
    LESS_THAN_OR_EQUAL_TO("lte"),
    NULL("null"),
    EMPTY("empty"),
    BETWEEN("btw");

    public static final Map<String, FilterOperation> OPERATIONS_MAP = Arrays.stream(FilterOperation.values())
            .collect(Collectors.toMap(FilterOperation::getSuffix, Function.identity()));

    private final String suffix;

    /**
     * @param suffix the suffix to check, e.g. "eq"
     * @return the FilterOperation for the given suffix or null if not found
     */
    public static FilterOperation fromSuffix(String suffix) {
        return OPERATIONS_MAP.get(suffix);
    }

    /**
     * @param suffix the suffix to check, e.g. "eq"
     * @return true if the suffix is supported, false otherwise
     */
    public static boolean isSupported(String suffix) {
        return OPERATIONS_MAP.containsKey(suffix);
    }

}
