package com.xlr8code.server.search.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

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

}
