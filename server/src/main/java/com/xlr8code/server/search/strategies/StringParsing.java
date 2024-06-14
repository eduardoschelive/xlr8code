package com.xlr8code.server.search.strategies;

import com.xlr8code.server.search.enums.SearchOperation;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

public class StringParsing implements ParsingStrategy {

    @Override
    public Predicate buildPredicate(CriteriaBuilder criteriaBuilder, Path<?> path, String fieldName, SearchOperation searchOperation, String value) {
        return switch (searchOperation) {
            case EQUALITY -> criteriaBuilder.equal(path, value);
            case NEGATION -> criteriaBuilder.notEqual(path, value);
            case GREATER_THAN -> criteriaBuilder.greaterThan(path.as(String.class), value);
            case LESS_THAN -> criteriaBuilder.lessThan(path.as(String.class), value);
            case CONTAINS -> criteriaBuilder.like(path.as(String.class), "%" + value + "%");
            case STARTS_WITH -> criteriaBuilder.like(path.as(String.class), value + "%");
            case ENDS_WITH -> criteriaBuilder.like(path.as(String.class), "%" + value);
            default -> throw new IllegalArgumentException("Unsupported operation: " + searchOperation);
        };
    }

}
