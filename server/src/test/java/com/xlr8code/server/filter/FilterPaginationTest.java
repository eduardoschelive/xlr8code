package com.xlr8code.server.filter;

import com.xlr8code.server.common.utils.PageUtils;
import com.xlr8code.server.filter.exception.PageNumberFormatException;
import com.xlr8code.server.filter.exception.PageSizeFormatException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.xlr8code.server.filter.utils.FilterConstants.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class FilterPaginationTest {

    @Test
    void it_should_create_pagination_object() {

        var paginationParams = Map.of(
                "page", "1",
                "size", "10"
        );

        var filterPagination = new FilterPagination(paginationParams);
        var pagination = filterPagination.getPageRequest();

        assertNotNull(pagination);
    }

    @Test
    void it_should_throw_exception_when_page_is_not_valid() {

        var paginationParams = Map.of(
                "page", "0",
                "size", "10"
        );

        var filterPagination = new FilterPagination(paginationParams);

        assertThrows(PageNumberFormatException.class, filterPagination::getPageRequest);
    }

    @Test
    void it_should_throw_exception_when_size_is_not_valid() {

        var paginationParams = Map.of(
                "page", "1",
                "size", "0"
        );

        var filterPagination = new FilterPagination(paginationParams);

        assertThrows(PageSizeFormatException.class, filterPagination::getPageRequest);
    }

    @Test
    void it_should_throw_exception_when_size_is_greater_than_max_size() {

        var paginationParams = Map.of(
                "page", "1",
                "size", String.valueOf(MAX_SIZE + 1)
        );

        var filterPagination = new FilterPagination(paginationParams);

        assertThrows(PageSizeFormatException.class, filterPagination::getPageRequest);
    }

    @Test
    void it_should_return_default_page_and_size_when_pagination_params_are_not_present() {
        Map<String, String> paginationParams = Map.of();

        var filterPagination = new FilterPagination(paginationParams);
        var pagination = filterPagination.getPageRequest();

        assertEquals(PageUtils.zeroIndexPage(DEFAULT_PAGE), pagination.getPageNumber());
        assertEquals(DEFAULT_SIZE, pagination.getPageSize());
    }

    @Test
    void it_should_throw_exception_when_page_is_not_a_number() {

        var paginationParams = Map.of(
                "page", "invalid",
                "size", "10"
        );

        var filterPagination = new FilterPagination(paginationParams);

        assertThrows(PageNumberFormatException.class, filterPagination::getPageRequest);
    }

    @Test
    void it_should_throw_exception_when_size_is_not_a_number() {

        var paginationParams = Map.of(
                "page", "1",
                "size", "invalid"
        );

        var filterPagination = new FilterPagination(paginationParams);

        assertThrows(PageSizeFormatException.class, filterPagination::getPageRequest);
    }

}