package com.xlr8code.server.filter;

import com.xlr8code.server.filter.exception.InvalidSortDirectionException;
import com.xlr8code.server.filter.exception.NoSuchSortableFieldException;
import com.xlr8code.server.filter.utils.FilterFieldDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class FilterSortingTest {

    private Map<String, FilterFieldDetails> filterDetailsMap;

    @BeforeEach
    void setUp() {

        filterDetailsMap = Map.of(
                "name", new FilterFieldDetails(String.class, "name"),
                "age", new FilterFieldDetails(Integer.class, "age")
        );

    }

    @Test
    void it_should_create_sort_object() {

        var sortParams = Map.of(
                "name_sort", "asc"
        );

        var filterSorting = new FilterSorting(sortParams, filterDetailsMap);

        var sort = filterSorting.getSort();

        assertNotNull(sort);
    }

    @Test
    void it_should_throw_exception_when_sort_field_is_not_valid() {

        var sortParams = Map.of(
                "invalid_field_sort", "asc"
        );

        var filterSorting = new FilterSorting(sortParams, filterDetailsMap);

        assertThrows(NoSuchSortableFieldException.class, filterSorting::getSort);
    }

    @Test
    void it_should_throw_exception_when_sort_order_is_not_valid() {

        var sortParams = Map.of(
                "name_sort", "invalid_order"
        );

        var filterSorting = new FilterSorting(sortParams, filterDetailsMap);

        assertThrows(InvalidSortDirectionException.class, filterSorting::getSort);
    }

    @Test
    void it_should_return_empty_sort_when_sort_params_are_empty() {
        Map<String,String> sortParams = Map.of();

        var filterSorting = new FilterSorting(sortParams, filterDetailsMap);

        var sort = filterSorting.getSort();

        assertNotNull(sort);
        assertTrue(sort.isUnsorted());
    }

}