package com.xlr8code.server.filter.strategies;

import com.xlr8code.server.filter.enums.FilterOperation;
import com.xlr8code.server.filter.utils.FilterOperationDetails;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

public class StringParsingStrategy extends ParsingStrategy {

    private static final String WILDCARD = "%";

    @Override
    public Predicate buildPredicate(CriteriaBuilder criteriaBuilder, Root<?> root, String fieldName, FilterOperationDetails filterOperationDetails, Object value) {
        String stringValue = (String) value;
        Expression<String> casedPath = getCasedPath(criteriaBuilder, root, fieldName, filterOperationDetails.isCaseInsensitive());
        String casedValue = getCasedValue(stringValue, filterOperationDetails.isCaseInsensitive());

        Predicate predicate = createPredicate(criteriaBuilder, casedPath, filterOperationDetails.filterOperation(), casedValue);
        if (predicate == null) {
            return super.buildPredicate(criteriaBuilder, root, fieldName, filterOperationDetails, stringValue);
        }

        return filterOperationDetails.negated() ? criteriaBuilder.not(predicate) : predicate;
    }

    @Override
    public List<FilterOperation> getSupportedFilterOperations() {
        var superSupportedFilterOperations = super.getSupportedFilterOperations();
        var supportedFilterOperations = new ArrayList<>(superSupportedFilterOperations);

        supportedFilterOperations.addAll(List.of(
                FilterOperation.STARTS_WITH,
                FilterOperation.ENDS_WITH,
                FilterOperation.LIKE,
                FilterOperation.IN
        ));

        return supportedFilterOperations;
    }

    private Expression<String> getCasedPath(CriteriaBuilder criteriaBuilder, Root<?> root, String fieldName, boolean isCaseInsensitive) {
        Expression<String> path = super.getPath(root, fieldName).as(String.class);
        return isCaseInsensitive ? criteriaBuilder.lower(path) : path;
    }

    private String getCasedValue(String value, boolean isCaseInsensitive) {
        return isCaseInsensitive ? value.toLowerCase() : value;
    }

    private Predicate createPredicate(CriteriaBuilder criteriaBuilder, Expression<String> casedPath, FilterOperation filterOperation, String casedValue) {
        return switch (filterOperation) {
            case EQUALITY -> criteriaBuilder.equal(casedPath, casedValue);
            case STARTS_WITH, ENDS_WITH, LIKE ->
                    criteriaBuilder.like(casedPath, getPattern(filterOperation, casedValue));
            case IN -> createInClause(criteriaBuilder, casedPath, casedValue);
            default -> null;
        };
    }

    private CriteriaBuilder.In<String> createInClause(CriteriaBuilder criteriaBuilder, Expression<String> casedPath, String casedValue) {
        CriteriaBuilder.In<String> inClause = criteriaBuilder.in(casedPath);
        for (String item : casedValue.split(",")) {
            inClause.value(item);
        }
        return inClause;
    }

    private String getPattern(FilterOperation operation, String value) {
        return switch (operation) {
            case STARTS_WITH -> value + WILDCARD;
            case ENDS_WITH -> WILDCARD + value;
            default -> WILDCARD + value + WILDCARD;
        };
    }

}
