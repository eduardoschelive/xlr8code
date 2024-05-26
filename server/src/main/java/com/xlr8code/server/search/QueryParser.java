package com.xlr8code.server.search;

import com.xlr8code.server.search.exception.SearchException;
import com.xlr8code.server.search.model.SearchParams;

import java.util.Map;

public class QueryParser {

    private static final String SEARCH_PARAM_SEPARATOR = "_";

    private final Map<String, String> queryParameters;
    private final SearchParams searchParams;

    public QueryParser(Map<String, String> queryParameters, Class<?> targetClass) {
        this.queryParameters = queryParameters;
        this.searchParams = new SearchParams(targetClass);
    }

    public void parseQueryParameters() throws SearchException {
        for (var entry : queryParameters.entrySet()) {
            var fieldPath = extractFieldPath(entry.getKey());
            var operation = extractOperation(entry.getKey());

            if (fieldPath == null || operation == null) {
                throw new SearchException("Invalid search parameter: " + entry.getKey());
            }

            searchParams.processParam(operation, fieldPath, entry.getValue());
        }

        System.out.println(searchParams.getFilterParams());
    }

    private String extractFieldPath(String key) {
        var separatorIndex = key.lastIndexOf(SEARCH_PARAM_SEPARATOR);
        return separatorIndex != -1 ? key.substring(0, separatorIndex) : null;
    }

    private String extractOperation(String key) {
        var separatorIndex = key.lastIndexOf(SEARCH_PARAM_SEPARATOR);
        return separatorIndex != -1 ? key.substring(separatorIndex + 1) : null;
    }
}
