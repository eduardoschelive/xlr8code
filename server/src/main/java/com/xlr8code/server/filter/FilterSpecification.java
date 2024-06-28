package com.xlr8code.server.filter;

import com.xlr8code.server.common.utils.StringUtils;
import com.xlr8code.server.filter.enums.FilterOperation;
import com.xlr8code.server.filter.exception.BadFilterFormatException;
import com.xlr8code.server.filter.exception.NoSuchFilterableFieldException;
import com.xlr8code.server.filter.exception.UnsupportedFilterOperationException;
import com.xlr8code.server.filter.strategies.ParsingStrategySelector;
import com.xlr8code.server.filter.utils.FilterOperationDetails;
import com.xlr8code.server.filter.utils.FilterUtils;
import com.xlr8code.server.filter.utils.FilterableFieldDetails;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Map;

import static com.xlr8code.server.filter.utils.FilterConstants.*;

@RequiredArgsConstructor
public class FilterSpecification<E> implements Specification<E> {

    private final Map<String, String> queryParameters;
    private final Map<String, FilterableFieldDetails> fieldDetailsMap;

    @Override
    public Predicate toPredicate(Root<E> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = queryParameters.entrySet().stream()
                .map(entry -> createPredicate(entry, root, criteriaBuilder))
                .toList();
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private Predicate createPredicate(Map.Entry<String, String> entry, Root<E> root, CriteriaBuilder criteriaBuilder) {
        String fieldPath = FilterUtils.extractFieldPath(entry.getKey());
        String operation = FilterUtils.extractOperation(entry.getKey());

        validateFilterParameter(fieldPath, operation);
        validateSearchableField(fieldPath);

        var filterableFieldDetails = fieldDetailsMap.get(fieldPath);

        var expectedType = filterableFieldDetails.fieldType();
        var parser = ParsingStrategySelector.getStrategy(expectedType);
        var filterOperationDetails = fromSuffix(operation);
        return parser.buildPredicate(criteriaBuilder, root, filterableFieldDetails.fieldPath(), filterOperationDetails, entry.getValue());
    }

    private FilterOperationDetails fromSuffix(String suffix) {
        var lowerCaseSuffix = suffix.toLowerCase();
        boolean negated = lowerCaseSuffix.startsWith(NEGATION_PREFIX);
        boolean caseInsensitive = lowerCaseSuffix.endsWith(CASE_INSENSITIVE_SUFFIX);

        if (negated) {
            lowerCaseSuffix = StringUtils.stripPrefix(lowerCaseSuffix, NEGATION_PREFIX);
        }

        if (caseInsensitive) {
            lowerCaseSuffix = StringUtils.stripSuffix(lowerCaseSuffix, CASE_INSENSITIVE_SUFFIX);
        }

        var operation = FilterOperation.fromSuffix(lowerCaseSuffix);

        return new FilterOperationDetails(operation, negated, caseInsensitive);
    }

    private void validateFilterParameter(String fieldPath, String operation) {
        if (fieldPath == null || operation == null) {
            throw new BadFilterFormatException();
        }

        if (!isSupported(operation)) {
            throw new UnsupportedFilterOperationException(operation);
        }

    }

    private void validateSearchableField(String fieldPath) {
        if (!fieldDetailsMap.containsKey(fieldPath)) {
            throw new NoSuchFilterableFieldException(fieldPath);
        }
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

}
