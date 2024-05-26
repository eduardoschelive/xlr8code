package com.xlr8code.server.search.model;

import com.xlr8code.server.search.enums.SearchSortType;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class SortingParams {

    private static final String SORT_SUFFIX = "sort";
    private static final Map<String, SearchSortType> VALUES_ENUM = Arrays.stream(SearchSortType.values())
            .collect(Collectors.toMap(SearchSortType::getValue, searchSortType -> searchSortType));

    private final Map<String, SearchSortType> sort = new HashMap<>();

    public static boolean isSupported(String suffix) {
        return SORT_SUFFIX.equalsIgnoreCase(suffix);
    }

    public void computeSort(String field, String value) {
        String lowerCaseValue = value.toLowerCase();
        SearchSortType sortType = VALUES_ENUM.get(lowerCaseValue);
        if (sortType != null) {
            this.sort.put(field, sortType);
        } else {
            throw new IllegalArgumentException("No such SearchSortType with value: " + value + ". Valid values are: " + VALUES_ENUM.keySet());
        }
    }

    @Override
    public String toString() {
        return "SortingParams(sort=" + this.sort + ")";
    }

}