package com.xlr8code.server.filter.utils;

import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xlr8code.server.filter.utils.FilterConstants.*;

/**
 * A class to hold the details of the query parameters.
 * <p>
 *     The query parameters are split into three categories:
 *     <ul>
 *         <li>Pagination parameters (page and size)</li>
 *         <li>Sort parameters (sort)</li>
 *          <li>Filter parameters: The parameters that are not pagination or sort parameters</li>
 *     </ul>
 * </p>
 *
 */
@Getter
public class QueryParameterDetails {

    private static final List<String> PAGINATION_PARAMS = List.of(PAGE_PARAM, SIZE_PARAM);
    private static final String SORT_SUFFIX = SEARCH_PARAM_SEPARATOR + SORT_PARAM;
    private final Map<String, String> filterParameters = new HashMap<>();
    private final Map<String, String> paginationParameters = new HashMap<>();
    private final Map<String, String> sortParameters = new HashMap<>();

    /**
     * @param parameters all the query parameters of the request
     */
    public QueryParameterDetails(Map<String, String> parameters) {
        splitParameters(parameters);
    }

    private static boolean isSortParameter(String key) {
        return key.endsWith(SORT_SUFFIX);
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

    private boolean isPaginationParameter(String key) {
        return PAGINATION_PARAMS.contains(key);
    }

}
