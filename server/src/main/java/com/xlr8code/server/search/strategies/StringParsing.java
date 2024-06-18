package com.xlr8code.server.search.strategies;

import com.xlr8code.server.search.enums.FilterOperation;
import com.xlr8code.server.search.utils.FilterOperationDetails;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.Arrays;

public class StringParsing extends ParsingStrategy {

    private static final String WILDCARD = "%";

    @Override
    public Predicate buildPredicate(CriteriaBuilder criteriaBuilder, Root<?> root, String fieldName, FilterOperationDetails filterOperationDetails, String value) {
        var isNegated = filterOperationDetails.negated();
        var isCaseInsensitive = filterOperationDetails.isCaseInsensitive();
        var path = root.get(fieldName);
        var operation = filterOperationDetails.filterOperation();

        var casedValue = isCaseInsensitive ? value.toLowerCase() : value;
        var casedPath = criteriaBuilder.lower(path.as(String.class));

        var predicate = getPredicate(criteriaBuilder, casedPath, operation, casedValue);
        if (predicate == null) {
            return super.buildPredicate(criteriaBuilder, root, fieldName, filterOperationDetails, value);
        }

        return isNegated ? criteriaBuilder.not(predicate) : predicate;
    }

    private Predicate getPredicate(CriteriaBuilder criteriaBuilder, Expression<String> casedPath, FilterOperation filterOperation, String casedValue) {
        return switch (filterOperation) {
            case EQUALITY -> criteriaBuilder.equal(casedPath, casedValue);
            case STARTS_WITH, ENDS_WITH, LIKE -> criteriaBuilder.like(casedPath, getOperationPattern(filterOperation, casedValue));
            case IN -> getInClause(criteriaBuilder, casedPath, casedValue);
            default -> null;
        };
    }

    private static CriteriaBuilder.In<String> getInClause(CriteriaBuilder criteriaBuilder, Expression<String> casedPath, String casedValue) {
        return criteriaBuilder.in(casedPath).value(Arrays.toString(casedValue.split(",")));
    }

    private String getOperationPattern(FilterOperation operation, String value) {
        return switch (operation) {
            case STARTS_WITH -> value + WILDCARD;
            case ENDS_WITH -> WILDCARD + value;
            default -> WILDCARD + value + WILDCARD;
        };
    }
}
