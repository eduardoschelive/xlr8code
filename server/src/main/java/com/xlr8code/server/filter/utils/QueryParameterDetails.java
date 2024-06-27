package com.xlr8code.server.filter.utils;

import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xlr8code.server.filter.utils.FilterConstants.*;

@Getter
public class QueryParameterDetails {

    private final Map<String, String> filterParameters = new HashMap<>();
    private final Map<String, String> paginationParameters = new HashMap<>();
    private final Map<String, String> sortParameters = new HashMap<>();

    private static final List<String> PAGINATION_PARAMS = List.of(PAGE_PARAM, SIZE_PARAM);
    private static final String SORT_SUFFIX = SEARCH_PARAM_SEPARATOR + SORT_PARAM;

    public QueryParameterDetails(Map<String, String> parameters) {
        splitParameters(parameters);
    }

    private void splitParameters(Map<String, String> parameters) {
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (isPaginationParameter(key)) {
                paginationParameters.put(key, value);
            } else if (isSortParameter(key)) {
                sortParameters.put(key, value);
            } else {
                filterParameters.put(key, value);
            }
        }
    }

    private static boolean isSortParameter(String key) {
        return key.endsWith(SORT_SUFFIX);
    }

    private boolean isPaginationParameter(String key) {
        return PAGINATION_PARAMS.contains(key);
    }

}
