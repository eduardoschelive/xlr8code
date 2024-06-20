package com.xlr8code.server.filter.strategies;

import com.xlr8code.server.filter.exception.UnsupportedFilterOperationOnFieldException;
import com.xlr8code.server.filter.utils.FilterOperationDetails;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
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
        var path = getPath(root, fieldName);
        return switch (filterOperationDetails.filterOperation()) {
            case EQUALITY -> criteriaBuilder.equal(path, value);
            case IN -> getInClause(criteriaBuilder, path, value);
            case NULL -> criteriaBuilder.isNull(path);
            default ->
                    throw new UnsupportedFilterOperationOnFieldException( filterOperationDetails.filterOperation().getSuffix(), fieldName);
        };
    }

    public CriteriaBuilder.In<Object> getInClause(CriteriaBuilder criteriaBuilder, Path<?> path, String value) {
        var inClause = criteriaBuilder.in(path.as(Object.class));
        for (var item : value.split(",")) {
            inClause.value(item);
        }
        return inClause;
    }

    public Path<Object> getPath(Root<?> root, String fieldName) {
        if (fieldName.contains(".")) {
            var parts = fieldName.split("\\.");
            var path = root.get(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                path = path.get(parts[i]);
            }
            return path;
        } else {
            return root.get(fieldName);
        }
    }

}
