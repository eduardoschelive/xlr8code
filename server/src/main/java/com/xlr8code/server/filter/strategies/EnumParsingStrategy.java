package com.xlr8code.server.filter.strategies;

import com.xlr8code.server.filter.enums.FilterOperation;
import com.xlr8code.server.filter.utils.FilterOperationDetails;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
public class EnumParsingStrategy<T extends Enum<T>> extends ParsingStrategy {

    private final Function<String, T> enumParser;
    private final StringParsingStrategy stringParsingStrategy = new StringParsingStrategy();

    @Override
    public Predicate buildPredicate(CriteriaBuilder criteriaBuilder, Root<?> root, String fieldName, FilterOperationDetails filterOperationDetails, Object value) {
        var stringValue = value.toString().trim();
        var enumValue = enumParser.apply(stringValue);
        return stringParsingStrategy.buildPredicate(criteriaBuilder, root, fieldName, filterOperationDetails, enumValue.name());
    }

    @Override
    public List<FilterOperation> getSupportedFilterOperations() {
        return stringParsingStrategy.getSupportedFilterOperations();
    }

}
