package com.xlr8code.server.search.strategies;

import com.xlr8code.server.search.enums.FilterOperation;
import com.xlr8code.server.search.utils.FilterOperationDetails;
import jakarta.persistence.criteria.*;

public class StringParsing extends ParsingStrategy {

    private static final String WILDCARD = "%";

    @Override
    public Predicate buildPredicate(CriteriaBuilder criteriaBuilder, Root<?> root, String fieldName, FilterOperationDetails filterOperationDetails, String value) {
        var isNegated = filterOperationDetails.negated();
        var isCaseInsensitive = filterOperationDetails.isCaseInsensitive();
        var path = super.getPath(root, fieldName).as(String.class);
        var operation = filterOperationDetails.filterOperation();

        var casedValue = isCaseInsensitive ? value.toLowerCase() : value;
        var casedPath = criteriaBuilder.lower(path);

        var predicate = getPredicate(criteriaBuilder, casedPath, operation, casedValue);
        if (predicate == null) {
            return super.buildPredicate(criteriaBuilder, root, fieldName, filterOperationDetails, value);
        }

        return isNegated ? criteriaBuilder.not(predicate) : predicate;
    }

    private Predicate getPredicate(CriteriaBuilder criteriaBuilder, Expression<String> casedPath, FilterOperation filterOperation, String casedValue) {
        return switch (filterOperation) {
            case EQUALITY -> criteriaBuilder.equal(casedPath, casedValue);
            case STARTS_WITH, ENDS_WITH, LIKE ->
                    criteriaBuilder.like(casedPath, getOperationPattern(filterOperation, casedValue));
            case IN -> getInClause(criteriaBuilder, casedPath, casedValue);

            default -> null;
        };
    }

    private static CriteriaBuilder.In<String> getInClause(CriteriaBuilder criteriaBuilder, Expression<String> casedPath, String casedValue) {
        var in = criteriaBuilder.in(casedPath);
        for (var item : casedValue.split(",")) {
            in.value(item);
        }
        return in;
    }

    private String getOperationPattern(FilterOperation operation, String value) {
        return switch (operation) {
            case STARTS_WITH -> value + WILDCARD;
            case ENDS_WITH -> WILDCARD + value;
            default -> WILDCARD + value + WILDCARD;
        };
    }
}
