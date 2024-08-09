package com.xlr8code.server.filter.strategies;

import com.xlr8code.server.filter.enums.FilterOperation;
import com.xlr8code.server.filter.exception.UnsupportedFilterOperationOnFieldException;
import com.xlr8code.server.filter.utils.FilterOperationDetails;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public class InstantParsingStrategy extends ParsingStrategy {

    @Override
    public Predicate buildPredicate(CriteriaBuilder criteriaBuilder, Root<?> root, String fieldName, FilterOperationDetails filterOperationDetails, Object value) {
        var stringValue = value.toString().trim();
        var instantValue = Instant.parse(stringValue);

        var predicate = createPredicate(criteriaBuilder, root, fieldName, filterOperationDetails, instantValue);
        if (predicate == null) {
            return super.buildPredicate(criteriaBuilder, root, fieldName, filterOperationDetails, instantValue);
        }
        return filterOperationDetails.negated() ? criteriaBuilder.not(predicate) : predicate;
    }

    private Predicate createPredicate(CriteriaBuilder criteriaBuilder, Root<?> root, String fieldName, FilterOperationDetails filterOperationDetails, Object value) {
        var path = getPath(root, fieldName);
        return switch (filterOperationDetails.filterOperation()) {
            case EQUALITY -> criteriaBuilder.equal(path, LocalDateTime.ofInstant((Instant) value, ZoneId.systemDefault()));
            case NULL -> criteriaBuilder.isNull(path);
            default -> null;
        };
    }

    @Override
    public List<FilterOperation> getSupportedFilterOperations() {
        return super.getSupportedFilterOperations();
    }



}
