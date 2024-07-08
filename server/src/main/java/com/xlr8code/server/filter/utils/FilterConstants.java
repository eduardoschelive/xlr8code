package com.xlr8code.server.filter.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilterConstants {

    public static final String SEARCH_PARAM_SEPARATOR = "_";
    public static final String NEGATION_PREFIX = "n-";
    public static final String CASE_INSENSITIVE_SUFFIX = "-i";

    public static final String PAGE_PARAM = "page";
    public static final String SIZE_PARAM = "size";
    public static final String SORT_PARAM = "sort";

    public static final int DEFAULT_PAGE = 1;
    public static final int DEFAULT_SIZE = 10;
    public static final int MAX_SIZE = 1000;

    public static final List<String> ACCEPTED_SORT_VALUES = List.of("asc", "desc");

}
