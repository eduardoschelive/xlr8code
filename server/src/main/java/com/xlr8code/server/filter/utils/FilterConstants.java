package com.xlr8code.server.filter.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilterConstants {

    public static final String SEARCH_PARAM_SEPARATOR = "_";
    public static final String NEGATION_PREFIX = "n-";
    public static final String CASE_INSENSITIVE_SUFFIX = "-i";

    public static final String PAGE_PARAM = "page";
    public static final String SIZE_PARAM = "size";
    public static final String SORT_PARAM = "sort";

}
