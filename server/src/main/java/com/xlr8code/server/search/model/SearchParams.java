package com.xlr8code.server.search.model;

import com.xlr8code.server.search.annotation.Searchable;
import com.xlr8code.server.search.strategies.ParsingStrategySelector;
import com.xlr8code.server.search.utils.SearchUtils;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.util.Pair;

import java.util.Map;

@Getter
public class SearchParams {

    private final Map<String, Pair<Searchable, Class<?>>> searchableFields;

    private final PaginationParams paginationParams;
    private final SortingParams sortingParams;
    private final FilterParams filterParams;

    public SearchParams(Class<?> targetClass) {
        this.paginationParams = new PaginationParams();
        this.sortingParams = new SortingParams();
        this.filterParams = new FilterParams();
        this.searchableFields = SearchUtils.extractSearchableFields(targetClass);
    }

    public void processParam(String operation, String field, String value) {

        if (PaginationParams.isParamSupported(operation)) {
            paginationParams.setParam(operation, value);
            return;
        }

        if (SortingParams.isSupported(operation)) {
            validateSearchableField(field);
            sortingParams.computeSort(field, value);
            return;
        }

        if (FilterParams.isSupported(operation)) {
            validateSearchableField(field);
            var expectedType = searchableFields.get(field).getSecond();
            filterParams.computeIfAbsent(operation, field, value, expectedType);
            return;
        }

        throw new IllegalArgumentException("Invalid search parameter: " + operation);
    }

    private void validateSearchableField(String fieldPath) {
        if (!isSearchableField(fieldPath)) {
            throw new IllegalArgumentException("No such searchable field: " + fieldPath);
        }
    }

    private boolean isSearchableField(String fieldPath) {
        return searchableFields.containsKey(fieldPath);
    }

}
