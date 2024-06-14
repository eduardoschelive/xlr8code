package com.xlr8code.server.search.strategies;

import com.xlr8code.server.search.enums.SearchOperation;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

public class StringParsing implements ParsingStrategy {

    @Override
    public Predicate buildPredicate(CriteriaBuilder criteriaBuilder, Path<?> path, String fieldName, SearchOperation searchOperation, String value) {
        return null;
    }

}
