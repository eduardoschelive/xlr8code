package com.xlr8code.server.search;

import com.xlr8code.server.search.annotation.Searchable;
import com.xlr8code.server.search.exception.SearchException;
import com.xlr8code.server.search.model.SearchOperation;
import com.xlr8code.server.search.model.SearchPagination;
import com.xlr8code.server.search.utils.SearchUtils;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryParser {

    private static final String SEARCH_PARAM_SEPARATOR = "_";

    private final Map<String, String> queryParameters;
    private final Map<String, Pair<Searchable, Class<?>>> searchableFields;
    private final Map<String, List<Pair<String, String>>> parsedSearchParams;

    public QueryParser(Map<String, String> queryParameters, Class<?> targetClass) throws SearchException {
        this.queryParameters = queryParameters;
        this.searchableFields = SearchUtils.extractSearchableFields(targetClass);
        this.parsedSearchParams = new HashMap<>();
        parseQueryParameters();
    }

    private void parseQueryParameters() throws SearchException {
        for (Map.Entry<String, String> entry : queryParameters.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String operation = extractOperation(key);
            String field = extractField(key);
            validateKey(field, operation);
            parsedSearchParams.computeIfAbsent(operation, k -> new ArrayList<>()).add(Pair.of(field, value));
        }
        System.out.println(parsedSearchParams);
    }

    private String extractField(String key) {
        return key.contains(SEARCH_PARAM_SEPARATOR) ? key.substring(0, key.lastIndexOf(SEARCH_PARAM_SEPARATOR)) : key;
    }

    private String extractOperation(String key) {
        return key.contains(SEARCH_PARAM_SEPARATOR) ? key.substring(key.lastIndexOf(SEARCH_PARAM_SEPARATOR) + 1) : "";
    }

    private void validateKey(String field, String operation) throws SearchException {
        if (!SearchOperation.isSupported(operation) && !SearchPagination.isSupported(operation)) {
            throw new SearchException("Unsupported operation: " + operation);
        }
        if (shouldValidateField(operation) && !searchableFields.containsKey(field)) {
            throw new SearchException("Unsupported field: " + field);
        }
    }

    private boolean shouldValidateField(String operation) {
        return SearchPagination.fromSuffix(operation) == null || SearchPagination.fromSuffix(operation).isFieldDependant();
    }

}
