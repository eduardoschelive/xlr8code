package com.xlr8code.server.search;

import com.xlr8code.server.search.annotation.Searchable;
import com.xlr8code.server.search.exception.SearchException;
import com.xlr8code.server.search.model.SearchOperation;
import com.xlr8code.server.search.model.SearchPagination;
import com.xlr8code.server.search.model.SearchParam;
import com.xlr8code.server.search.utils.SearchUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class QueryParser {

    private final Map<String, String> queryMap;
    Map<String, Pair<Searchable, Class<?>>> searchableFields;
    Class<?> targetClass;

    public QueryParser(Map<String, String> queryMap, Class<?> targetClass) throws SearchException {
        this.queryMap = queryMap;
        this.targetClass = targetClass;
        this.searchableFields = SearchUtils.extractSearchableFields(targetClass);
        validateQuery();
    }

    public String extractField(String key) {
        return key.substring(0, key.lastIndexOf("_"));
    }

    public String extractOperation(String key) {
        var index = key.lastIndexOf("_");
        if (index == -1) {
            // TODO: throw custom exception
            throw new IllegalArgumentException("Missing operation in key: " + key);
        }
        return key.substring(index);
    }

    public void validateQuery() throws SearchException {
        for (String key : queryMap.keySet()) {
            validateKey(key);
        }
    }

    private void validateKey(String key) throws SearchException {
        var operation = extractOperation(key);
        validateOperation(operation);

        String field = extractField(key);
        validateField(field, operation);
    }

    private void validateOperation(String operation) throws SearchException {
        if (!SearchOperation.isSupported(operation) && !SearchPagination.isSupported(operation)) {
            throw new SearchException("Operation not supported: " + operation + ". Supported operations: " + SearchOperation.getOperations());
        }
    }

    private void validateField(String field, String operation) throws SearchException {
        var searchPagination = SearchPagination.fromSuffix(operation);
        boolean shouldValidateField = searchPagination == null || searchPagination.isFieldDependant();
        if (shouldValidateField && !searchableFields.containsKey(field)) {
            throw new SearchException("Field not found: " + field + ". Supported fields: " + searchableFields.keySet());
        }
    }

    public Sort getSort() {



        return null;
    }

}
