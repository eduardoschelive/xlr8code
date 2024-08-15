package com.xlr8code.server.filter.strategies;

import com.xlr8code.server.filter.enums.FilterOperation;
import com.xlr8code.server.filter.exception.UnsupportedFilterOperationOnFieldException;
import com.xlr8code.server.filter.utils.FilterOperationDetails;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.List;

/**
 * Abstract class that defines the parsing strategy for a filter operation.
 * <p>
 * This class is used to define the parsing strategy for a filter operation. The parsing strategy is used to build
 * the predicate for a filter operation.
 * The class defines the method {@link #buildPredicate(CriteriaBuilder, Root, String, FilterOperationDetails, Object)}
 * that is used to build the predicate for a filter operation.
 * </p>
 */
public abstract class ParsingStrategy {

    public Predicate buildPredicate(CriteriaBuilder criteriaBuilder,
                                    Root<?> root,
                                    String fieldName,
                                    FilterOperationDetails filterOperationDetails,
                                    Object value
    ) {
        var isNegated = filterOperationDetails.negated();

        var predicate = createPredicate(criteriaBuilder, root, fieldName, filterOperationDetails, value);
        return isNegated ? criteriaBuilder.not(predicate) : predicate;
    }

    private Predicate createPredicate(CriteriaBuilder criteriaBuilder, Root<?> root, String fieldName, FilterOperationDetails filterOperationDetails, Object value) {
        var path = getPath(root, fieldName);
        return switch (filterOperationDetails.filterOperation()) {
            case EQUALITY -> criteriaBuilder.equal(path, value);
            case NULL -> criteriaBuilder.isNull(path);
            default ->
                    throw new UnsupportedFilterOperationOnFieldException(filterOperationDetails.filterOperation().getSuffix(), fieldName);
        };
    }

    public <T> Path<T>  getPath(Root<?> root, String fieldName) {
        if (fieldName.contains(".")) {
            var parts = fieldName.split("\\.");
            var path = root.<T>get(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                path = path.get(parts[i]);
            }
            return path;
        } else {
            return root.get(fieldName);
        }
    }

    public List<FilterOperation> getSupportedFilterOperations() {
        return List.of(FilterOperation.EQUALITY, FilterOperation.NULL);
    }

}
