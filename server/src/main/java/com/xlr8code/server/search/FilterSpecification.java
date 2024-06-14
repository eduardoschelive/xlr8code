package com.xlr8code.server.search;

import com.xlr8code.server.search.annotation.Searchable;
import com.xlr8code.server.search.enums.SearchOperation;
import com.xlr8code.server.search.strategies.ParsingStrategySelector;
import com.xlr8code.server.search.utils.SearchUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FilterSpecification<E> implements Specification<E> {

    private static final String SEARCH_PARAM_SEPARATOR = "_";
    private final Map<String, String> queryParameters;
    private final Map<String, Pair<Searchable, Class<?>>> searchableFields;
    private static final Map<String, SearchOperation> OPERATIONS_MAP = Arrays.stream(SearchOperation.values())
            .collect(Collectors.toMap(SearchOperation::getSuffix, Function.identity()));

    public FilterSpecification(Map<String, String> queryParameters, Class<E> targetClass) {
        this.queryParameters = queryParameters;
        this.searchableFields = SearchUtils.extractSearchableFields(targetClass);
    }

    @Override
    public Predicate toPredicate(Root<E> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        for (var entry : queryParameters.entrySet()) {
            var fieldPath = extractFieldPath(entry.getKey());
            var operation = extractOperation(entry.getKey());

            if (fieldPath == null || operation == null) {
                throw new IllegalArgumentException("Invalid search parameter: " + entry.getKey());
            }

            if (isSupported(operation)) {
                validateSearchableField(fieldPath);
                var expectedType = searchableFields.get(fieldPath).getSecond();
                var parser = ParsingStrategySelector.getStrategy(expectedType);
                var predicate = parser.buildPredicate(criteriaBuilder, root.get(fieldPath), fieldPath, fromSuffix(operation), entry.getValue());
                predicates.add(predicate);
            }
        }

        return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
    }

    private String extractFieldPath(String key) {
        var separatorIndex = key.lastIndexOf(SEARCH_PARAM_SEPARATOR);
        return separatorIndex != -1 ? key.substring(0, separatorIndex).trim() : null;
    }

    private String extractOperation(String key) {
        var separatorIndex = key.lastIndexOf(SEARCH_PARAM_SEPARATOR);
        return separatorIndex != -1 ? key.substring(separatorIndex + 1).trim() : null;
    }

    private void validateSearchableField(String fieldPath) {
        if (!isSearchableField(fieldPath)) {
            throw new IllegalArgumentException("No such searchable field: " + fieldPath);
        }
    }

    private SearchOperation fromSuffix(String suffix) {
        SearchOperation result = OPERATIONS_MAP.get(suffix.toLowerCase());

        if (result == null) {
            throw new IllegalArgumentException("No such SearchOperation with suffix: " + suffix + ". Valid suffixes are: " + OPERATIONS_MAP.keySet());
        }

        return result;
    }

    private boolean isSupported(String suffix) {
        return OPERATIONS_MAP.containsKey(suffix.toLowerCase());
    }

    private boolean isSearchableField(String fieldPath) {
        return searchableFields.containsKey(fieldPath);
    }

}
