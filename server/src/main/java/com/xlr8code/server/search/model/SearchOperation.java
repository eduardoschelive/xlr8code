package com.xlr8code.server.search.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public enum SearchOperation {

    EQUALITY("_eq"),
    EQUALITY_CASE_INSENSITIVE("_eqi"),
    NEGATION("_neq"),
    NEGATION_CASE_INSENSITIVE("_neqi"),
    GREATER_THAN("_gt"),
    GREATER_THAN_OR_EQUAL_TO("_gte"),
    LESS_THAN("_lt"),
    LESS_THAN_OR_EQUAL_TO("_lte"),
    STARTS_WITH("_sw"),
    STARTS_WITH_CASE_INSENSITIVE("_swi"),
    ENDS_WITH("_ew"),
    ENDS_WITH_CASE_INSENSITIVE("_ewi"),
    CONTAINS("_ct"),
    CONTAINS_CASE_INSENSITIVE("_cti"),
    IN("_in"),
    NOT_IN("_nin"),
    IS_NULL("_null"),
    IS_EMPTY("_empty"),
    BETWEEN("_btw");

    private final String suffix;

    private static final Map<String, SearchOperation> SUFFIX_ENUM =
            Arrays.stream(SearchOperation.values())
                    .collect(Collectors.toMap(SearchOperation::getSuffix, Function.identity()));


    public static SearchOperation fromSuffix(String suffix) {
        var result = SUFFIX_ENUM.get(suffix.toLowerCase());

        if (result == null) {
            throw new IllegalArgumentException("No such SearchOperation with suffix: " + suffix + ". Valid suffixes are: " + SUFFIX_ENUM.keySet());
        }

        return result;
    }

    public static boolean isSupported(String suffix) {
        return SUFFIX_ENUM.containsKey(suffix.toLowerCase());
    }

    public static Set<String> getOperations() {
        return SUFFIX_ENUM.keySet();
    }
}
