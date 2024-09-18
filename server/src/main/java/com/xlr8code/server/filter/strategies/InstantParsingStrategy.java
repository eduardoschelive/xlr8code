package com.xlr8code.server.filter.strategies;

import com.xlr8code.server.filter.enums.FilterOperation;
import com.xlr8code.server.filter.exception.InvalidInstantBoundsException;
import com.xlr8code.server.filter.exception.RequiredParamSizeException;
import com.xlr8code.server.filter.utils.FilterOperationDetails;
import com.xlr8code.server.filter.utils.FilterParsingUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.xlr8code.server.filter.utils.FilterConstants.BETWEEN_FILTER_SIZE;

public class InstantParsingStrategy extends ParsingStrategy {

    @Override
    public Predicate buildPredicate(CriteriaBuilder criteriaBuilder, Root<?> root, String fieldName, FilterOperationDetails filterOperationDetails, Object value) {
        var stringValue = value.toString().trim();

        var predicate = createPredicate(criteriaBuilder, root, fieldName, filterOperationDetails, stringValue);
        if (predicate == null) {
            return super.buildPredicate(criteriaBuilder, root, fieldName, filterOperationDetails,  Instant.parse(stringValue));
        }
        return filterOperationDetails.negated() ? criteriaBuilder.not(predicate) : predicate;
    }

    private Predicate createPredicate(CriteriaBuilder criteriaBuilder, Root<?> root, String fieldName, FilterOperationDetails filterOperationDetails, String stringValue) {
       Path<Instant> path = getPath(root, fieldName);
        return switch (filterOperationDetails.filterOperation()) {
            case GREATER_THAN -> criteriaBuilder.greaterThan(path,  Instant.parse(stringValue));
            case GREATER_THAN_OR_EQUAL_TO -> criteriaBuilder.greaterThanOrEqualTo(path,  Instant.parse(stringValue));
            case LESS_THAN -> criteriaBuilder.lessThan(path,  Instant.parse(stringValue));
            case LESS_THAN_OR_EQUAL_TO -> criteriaBuilder.lessThanOrEqualTo(path,  Instant.parse(stringValue));
            case BETWEEN -> buildBetweenCriteria(criteriaBuilder, path, stringValue );
            default -> null;
        };
    }

    private static Predicate buildBetweenCriteria(CriteriaBuilder criteriaBuilder, Path<Instant> path, String stringValue) {
        // Split the string into two parts
        var values = FilterParsingUtils.parseFilterValues(stringValue);
        if (values.length != BETWEEN_FILTER_SIZE) {
            throw new RequiredParamSizeException(FilterOperation.BETWEEN.getSuffix(), BETWEEN_FILTER_SIZE);
        }

        var lowerBound = FilterParsingUtils.parseInstant(values[0].trim());
        var upperBound = FilterParsingUtils.parseInstant(values[1].trim());

        if (lowerBound.isAfter(upperBound)) {
            throw new InvalidInstantBoundsException(lowerBound.toString(), upperBound.toString());
        }

        return criteriaBuilder.between(path, lowerBound, upperBound);
    }

    @Override
    public List<FilterOperation> getSupportedFilterOperations() {
        var superSupportedFilterOperations = super.getSupportedFilterOperations();
        var supportedFilterOperations = new ArrayList<>(superSupportedFilterOperations);

        supportedFilterOperations.addAll(List.of(
                FilterOperation.GREATER_THAN,
                FilterOperation.GREATER_THAN_OR_EQUAL_TO,
                FilterOperation.LESS_THAN,
                FilterOperation.LESS_THAN_OR_EQUAL_TO,
                FilterOperation.BETWEEN
        ));

        return supportedFilterOperations;
    }



}
