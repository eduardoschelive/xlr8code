package com.xlr8code.server.filter.utils;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class QueryParameterDetailsTest {

    @Test
    void it_should_extract_pagination_params() {
        var queryParameters = Map.of(
                "page", "1",
                "size", "10",
                "sort", "name,asc",
                "filter_name_eq", "John",
                "filter_age_gt", "20"
        );

        var queryParamDetails = new QueryParameterDetails(queryParameters);

        var expectedPaginationParams = Map.of(
                "page", "1",
                "size", "10"
        );

        assertEquals(expectedPaginationParams, queryParamDetails.getPaginationParameters());
    }

    @Test
    void it_should_extract_sort_params() {
        var queryParameters = Map.of(
                "page", "1",
                "size", "10",
                "name_sort", "asc",
                "name_eq", "John",
                "age_gt", "20"
        );

        var queryParamDetails = new QueryParameterDetails(queryParameters);

        var expectedSortParams = Map.of(
                "name_sort", "asc"
        );

        assertEquals(expectedSortParams, queryParamDetails.getSortParameters());
    }

    @Test
    void it_should_extract_filter_params() {
        var queryParameters = Map.of(
                "page", "1",
                "size", "10",
                "name_sort", "asc",
                "name_eq", "John",
                "age_gt", "20"
        );

        var queryParamDetails = new QueryParameterDetails(queryParameters);

        var expectedFilterParams = Map.of(
                "name_eq", "John",
                "age_gt", "20"
        );

        assertEquals(expectedFilterParams, queryParamDetails.getFilterParameters());
    }

}