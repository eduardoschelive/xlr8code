package com.xlr8code.server.search;

import com.xlr8code.server.search.annotation.Searchable;
import com.xlr8code.server.search.exception.SearchException;
import com.xlr8code.server.search.model.*;
import com.xlr8code.server.search.utils.ParamHolder;
import com.xlr8code.server.search.utils.SearchUtils;
import org.springframework.data.util.Pair;

import java.util.Map;

public class QueryParser {

    private static final String SEARCH_PARAM_SEPARATOR = "_";

    private final Map<String, String> queryParameters;
    private final Class<?> targetClass;

    private final ParamHolder<SearchOperation, FieldValue<Object>> searchParams = new ParamHolder<>();
    private final ParamHolder<SearchPagination, Object> paginationParams = new ParamHolder<>();
    private final ParamHolder<SearchSorting, FieldValue<SearchSortType>> sortingParams = new ParamHolder<>();

    public QueryParser(Map<String, String> queryParameters, Class<?> targetClass) throws SearchException {
        this.queryParameters = queryParameters;
        this.targetClass = targetClass;
    }

    public void parseQueryParameters() throws SearchException {

        for (Map.Entry<String, String> entry : queryParameters.entrySet()) {
            var fieldPath = extractFieldPath(entry.getKey());
            var operation = extractOperation(entry.getKey());
            if (fieldPath == null || operation == null) {
                throw new SearchException("Invalid search parameter: " + entry.getKey());
            }

            if (SearchPagination.isSupported(operation)) {
                paginationParams.computeIfAbsent(SearchPagination.fromSuffix(operation), entry.getValue());
            }

            if (SearchSorting.isSupported(operation) && isSearchableField(fieldPath)) {
                processSortingParams(fieldPath, operation, entry.getValue());
            }

            if (SearchOperation.isSupported(operation) && isSearchableField(fieldPath)) {
                searchParams.computeIfAbsent(SearchOperation.fromSuffix(operation), new FieldValue<>(fieldPath, entry.getValue()));
            }

        }

        System.out.println(searchParams.getSearchParams());
        System.out.println(paginationParams.getSearchParams());
        System.out.println(sortingParams.getSearchParams());

    }

    private String extractFieldPath(String key) {
        return key.contains(SEARCH_PARAM_SEPARATOR) ?  key.substring(0, key.lastIndexOf(SEARCH_PARAM_SEPARATOR)) : null;
    }

    private String extractOperation(String key) {
        return key.contains(SEARCH_PARAM_SEPARATOR) ? key.substring(key.lastIndexOf(SEARCH_PARAM_SEPARATOR) + 1) : null;
    }

    private void processSortingParams(String fieldPath, String operation, String value) {
        var searchSorting = SearchSorting.fromSuffix(operation);
        sortingParams.computeIfAbsent(searchSorting, new FieldValue<>(fieldPath, SearchSortType.fromSuffix(value)));
    }

    private boolean isSearchableField(String fieldPath) {
        return SearchUtils.extractSearchableFields(targetClass).containsKey(fieldPath);
    }

}
