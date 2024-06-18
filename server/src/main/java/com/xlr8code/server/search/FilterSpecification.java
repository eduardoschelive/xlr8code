package com.xlr8code.server.search;

import com.xlr8code.server.common.utils.StringUtils;
import com.xlr8code.server.search.annotation.Searchable;
import com.xlr8code.server.search.enums.FilterOperation;
import com.xlr8code.server.search.strategies.ParsingStrategySelector;
import com.xlr8code.server.search.utils.FilterOperationDetails;
import com.xlr8code.server.search.utils.SearchUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Pair;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.xlr8code.server.search.utils.FilterConstants.*;

public class FilterSpecification<E> implements Specification<E> {

    private final Map<String, String> queryParameters;
    private final Map<String, Pair<Searchable, Class<?>>> searchableFields;

    public FilterSpecification(Map<String, String> queryParameters, Class<E> targetClass) {
        this.queryParameters = queryParameters;
        this.searchableFields = SearchUtils.extractSearchableFields(targetClass);
    }

    @Override
    public Predicate toPredicate(Root<E> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = queryParameters.entrySet().stream()
                .map(entry -> createPredicate(entry, root, criteriaBuilder))
                .toList();
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private Predicate createPredicate(Map.Entry<String, String> entry, Root<E> root, CriteriaBuilder criteriaBuilder) {
        String fieldPath = extractFieldPath(entry.getKey());
        String operation = extractOperation(entry.getKey());

        if (fieldPath == null || operation == null || !isSupported(operation)) {
            throw new IllegalArgumentException("Invalid search parameter: " + entry.getKey());
        }

        validateSearchableField(fieldPath);
        Class<?> expectedType = searchableFields.get(fieldPath).getSecond();
        var parser = ParsingStrategySelector.getStrategy(expectedType);
        var filterOperationDetails = fromSuffix(operation);
        return parser.buildPredicate(criteriaBuilder, root, fieldPath, filterOperationDetails, entry.getValue());
    }

    private void validateSearchableField(String fieldPath) {
        if (!searchableFields.containsKey(fieldPath)) {
            throw new IllegalArgumentException("No such searchable field: " + fieldPath);
        }
    }

    private FilterOperationDetails fromSuffix(String suffix) {
        String lowerCaseSuffix = suffix.toLowerCase();
        boolean negated = lowerCaseSuffix.startsWith(NEGATION_PREFIX);
        boolean caseInsensitive = lowerCaseSuffix.endsWith(CASE_INSENSITIVE_SUFFIX);

        if (negated) {
            lowerCaseSuffix = StringUtils.stripPrefix(lowerCaseSuffix, NEGATION_PREFIX);
        }

        if (caseInsensitive) {
            lowerCaseSuffix = StringUtils.stripSuffix(lowerCaseSuffix, CASE_INSENSITIVE_SUFFIX);
        }

        FilterOperation operation = FilterOperation.fromSuffix(lowerCaseSuffix);

        return new FilterOperationDetails(operation, negated, caseInsensitive);
    }

    private boolean isSupported(String suffix) {
        var normalizedSuffix = normalizeSuffix(suffix);
        return FilterOperation.isSupported(normalizedSuffix);
    }

    private String normalizeSuffix(String suffix) {
        var result = suffix.toLowerCase();
        result = StringUtils.stripPrefix(result, NEGATION_PREFIX);
        result = StringUtils.stripSuffix(result, CASE_INSENSITIVE_SUFFIX);
        return result;
    }
    private String extractFieldPath(String key) {
        int separatorIndex = key.lastIndexOf(SEARCH_PARAM_SEPARATOR);
        return (separatorIndex != -1) ? key.substring(0, separatorIndex).trim() : null;
    }

    private String extractOperation(String key) {
        int separatorIndex = key.lastIndexOf(SEARCH_PARAM_SEPARATOR);
        return (separatorIndex != -1) ? key.substring(separatorIndex + 1).trim() : null;
    }

}
