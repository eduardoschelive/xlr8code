package com.xlr8code.server.search.strategies;

import com.xlr8code.server.search.utils.FilterOperationDetails;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public abstract class ParsingStrategy {

    public Predicate buildPredicate(CriteriaBuilder criteriaBuilder,
                             Root<?> root,
                             String fieldName,
                                    FilterOperationDetails filterOperationDetails,
                             String value
    ) {
        var isNegated = filterOperationDetails.negated();

        var predicate = getPredicate(criteriaBuilder, root, fieldName, filterOperationDetails, value);

        return isNegated ? criteriaBuilder.not(predicate) : predicate;
    }

    private Predicate getPredicate(CriteriaBuilder criteriaBuilder, Root<?> root, String fieldName, FilterOperationDetails filterOperationDetails, String value) {
        return switch (filterOperationDetails.filterOperation()) {
            case EQUALITY -> criteriaBuilder.equal(root.get(fieldName), value);
            case IN -> getInClause(criteriaBuilder, root, fieldName, value);
            case NULL -> criteriaBuilder.isNull(root.get(fieldName));
            default -> throw new IllegalArgumentException("Unsupported operation: " + filterOperationDetails.filterOperation());
        };
    }

    public CriteriaBuilder.In<Object> getInClause(CriteriaBuilder criteriaBuilder, Root<?> root, String fieldName, String value) {
        var inClause = criteriaBuilder.in(root.get(fieldName));
        for (var item : value.split(",")) {
            inClause.value(item);
        }
        return inClause;
    }

}
