package com.xlr8code.server.filter;

import com.xlr8code.server.filter.entity.FilterTestEntity;
import com.xlr8code.server.filter.exception.*;
import com.xlr8code.server.filter.repository.FilterTestRepository;
import com.xlr8code.server.filter.utils.FilterTestUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class FilterTest {

    @Autowired
    private FilterTestRepository testRepository;

    private static final int TEST_SIZE = 10;

    @BeforeAll
    static void setup(@Autowired FilterTestUtils filterTestUtils) {
        filterTestUtils.createNEntities(TEST_SIZE);
    }

    @AfterAll
    static void cleanup(@Autowired FilterTestUtils filterTestUtils) {
        filterTestUtils.clearRepositories();
    }

    @Test
    void it_should_find_all() {
        List<FilterTestEntity> all = testRepository.findAll();
        assertEquals(TEST_SIZE, all.size());
    }

    @Test
    void it_should_filter_with_page_and_size() {
        var params = Map.of(
                "booleanField_eq", "true",
                "page", "1",
                "size", "5"
        );

        Page<FilterTestEntity> results = testRepository.findAll(params, FilterTestEntity.class);
        assertEquals(5, results.getTotalElements());
    }

    @Test
    void it_should_filter_with_sort() {
        var params = Map.of(
                "booleanField_eq", "true",
                "stringField_sort", "desc"
        );

        Page<FilterTestEntity> results = testRepository.findAll(params, FilterTestEntity.class);

        assertEquals(TEST_SIZE / 2, results.getTotalElements());
        assertEquals("stringField8", results.getContent().get(0).getStringField());
    }

    @Test
    void it_should_filter_with_nested_relation() {
        var params = Map.of(
                "testRelationEntity.stringRelationField_eq", "stringField00"
        );

        Page<FilterTestEntity> results = testRepository.findAll(params, FilterTestEntity.class);
        assertEquals(1, results.getTotalElements());
    }

    @Test
    void it_should_filter_with_nested_one_to_one_relation() {
        var params = Map.of(
                "testOneToOneRelationEntity.stringRelationField_eq", "stringField0"
        );

        Page<FilterTestEntity> results = testRepository.findAll(params, FilterTestEntity.class);
        assertEquals(1, results.getTotalElements());
    }

    @Test
    void it_should_throw_error_if_operation_not_supported_on_field() {
        var params = Map.of(
                "stringField_gt", "stringField0"
        );

        assertThrows(UnsupportedFilterOperationOnFieldException.class, () ->
                testRepository.findAll(params, FilterTestEntity.class));
    }

    @Test
    void it_should_filter_by_negated_boolean_field() {
        var params = Map.of(
                "booleanField_n-eq", "true"
        );

        Page<FilterTestEntity> results = testRepository.findAll(params, FilterTestEntity.class);
        assertEquals(TEST_SIZE / 2, results.getTotalElements());
    }

    @Test
    void it_should_throw_error_when_field_or_value_is_empty() {
        var params = Map.of("", "");

        assertThrows(BadFilterFormatException.class, () ->
                testRepository.findAll(params, FilterTestEntity.class));
    }

    @Test
    void it_should_throw_error_when_operation_is_invalid() {
        var params = Map.of(
                "stringField_invalid", "stringField0"
        );

        assertThrows(UnsupportedFilterOperationException.class, () ->
                testRepository.findAll(params, FilterTestEntity.class));
    }

    @Test
    void it_should_throw_error_when_field_is_invalid() {
        var params = Map.of(
                "invalidField_eq", "x"
        );

        assertThrows(NoSuchFilterableFieldException.class, () ->
                testRepository.findAll(params, FilterTestEntity.class));
    }

    @Test
    void it_should_throw_error_when_operation_is_empty() {
        var params = Map.of(
                "stringField", "stringField0"
        );

        assertThrows(BadFilterFormatException.class, () ->
                testRepository.findAll(params, FilterTestEntity.class));
    }

    @Test
    void it_should_throw_error_when_field_is_empty() {
        var params = Map.of(
                "eq", "stringField0"
        );

        assertThrows(BadFilterFormatException.class, () ->
                testRepository.findAll(params, FilterTestEntity.class));
    }
}
