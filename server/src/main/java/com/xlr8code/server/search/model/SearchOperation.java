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

    EQUALITY("eq"),
    EQUALITY_CASE_INSENSITIVE("eqi"),
    NEGATION("neq"),
    NEGATION_CASE_INSENSITIVE("neqi"),
    GREATER_THAN("gt"),
    GREATER_THAN_OR_EQUAL_TO("gte"),
    LESS_THAN("lt"),
    LESS_THAN_OR_EQUAL_TO("lte"),
    STARTS_WITH("sw"),
    STARTS_WITH_CASE_INSENSITIVE("swi"),
    ENDS_WITH("ew"),
    ENDS_WITH_CASE_INSENSITIVE("ewi"),
    CONTAINS("ct"),
    CONTAINS_CASE_INSENSITIVE("cti"),
    IN("in"),
    NOT_IN("nin"),
    IS_NULL("null"),
    IS_EMPTY("empty"),
    BETWEEN("btw");

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
