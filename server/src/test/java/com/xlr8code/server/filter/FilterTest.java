package com.xlr8code.server.filter;

import com.xlr8code.server.filter.entity.FilterOneToOneRelationTest;
import com.xlr8code.server.filter.entity.FilterRelationTest;
import com.xlr8code.server.filter.entity.FilterTestEntity;
import com.xlr8code.server.filter.exception.InvalidBooleanFilterValueException;
import com.xlr8code.server.filter.repository.FilterOneToOneRelationRepository;
import com.xlr8code.server.filter.repository.FilterRelationTestRepository;
import com.xlr8code.server.filter.repository.FilterTestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class FilterTest {

    // TODO: Refactor this test please
    @Autowired
    private FilterTestRepository testRepository;

    @Autowired
    private FilterRelationTestRepository relationTestRepository;

    @Autowired
    private FilterOneToOneRelationRepository oneToOneRelationRepository;

    private static final int TEST_SIZE = 10;

    @BeforeEach
    public void setUp() {
        IntStream.range(0, TEST_SIZE).forEach(i -> {
            FilterTestEntity entity = createAndSaveFilterTestEntity(i);
            IntStream.range(0, TEST_SIZE).forEach(j -> createAndSaveFilterRelationTest(entity, i, j));
            createAndSaveFilterOneToOneRelationTest(entity, i);
        });
    }

    private FilterTestEntity createAndSaveFilterTestEntity(int index) {
        return testRepository.save(FilterTestEntity.builder()
                .stringField("stringField" + index)
                .booleanField(index % 2 == 0)
                .build());
    }

    private void createAndSaveFilterRelationTest(FilterTestEntity entity, int i, int j) {
        relationTestRepository.save(FilterRelationTest.builder()
                .testEntity(entity)
                .stringRelationField("stringField" + i + j)
                .booleanRelationField(j % 2 == 0)
                .build());
    }

    private void createAndSaveFilterOneToOneRelationTest(FilterTestEntity entity, int i) {
        oneToOneRelationRepository.save(FilterOneToOneRelationTest.builder()
                .testEntity(entity)
                .stringRelationField("stringField" + i)
                .booleanRelationField(i % 2 == 0)
                .build());
    }

    @Test
    void it_should_find_all() {
        List<FilterTestEntity> all = testRepository.findAll();
        assertEquals(TEST_SIZE, all.size());
    }

    @Test
    void it_should_filter_by_string_field() {
        var params = Map.of("stringField_eq", "stringField1");

        Page<FilterTestEntity> all = testRepository.findAll(params, FilterTestEntity.class);

        assertEquals(1, all.getTotalElements());
    }

    @Test
    void it_should_filter_by_boolean_field() {
        var params = Map.of("booleanField_eq", "true");

        Page<FilterTestEntity> all = testRepository.findAll(params, FilterTestEntity.class);

        assertEquals(TEST_SIZE / 2, all.getTotalElements());
    }

    @Test
    void it_should_filter_with_page_and_size() {
        var params = Map.of("booleanField_eq", "true", "page", "1", "size", "5");

        Page<FilterTestEntity> all = testRepository.findAll(params, FilterTestEntity.class);

        assertEquals(5, all.getTotalElements());
    }

    @Test
    void it_should_filter_with_sort() {
        var params = Map.of("booleanField_eq", "true", "stringField_sort", "desc");

        Page<FilterTestEntity> all = testRepository.findAll(params, FilterTestEntity.class);

        assertEquals(TEST_SIZE / 2, all.getTotalElements());
        assertEquals("stringField8", all.getContent().getFirst().getStringField());
    }

    @Test
    void it_should_filter_with_nested_relation() {
        var params = Map.of("testRelationEntity.stringRelationField_eq", "stringField00");

        Page<FilterTestEntity> all = testRepository.findAll(params, FilterTestEntity.class);

        assertEquals(1, all.getTotalElements());
    }

    @Test
    void it_should_filter_with_nested_one_to_one_relation() {
        var params = Map.of("testOneToOneRelationEntity.stringRelationField_eq", "stringField0");

        Page<FilterTestEntity> all = testRepository.findAll(params, FilterTestEntity.class);

        assertEquals(1, all.getTotalElements());
    }


    @Test
    void it_should_filter_when_boolean_value_is_null() {
        var params = Map.of("booleanField_null", "");

        Page<FilterTestEntity> all = testRepository.findAll(params, FilterTestEntity.class);

        assertEquals(0, all.getTotalElements());
    }

    @Test
    void it_should_throw_exception_when_boolean_value_is_invalid() {
        var params = Map.of("booleanField_eq", "invalid");

        assertThrows(InvalidBooleanFilterValueException.class, () -> testRepository.findAll(params, FilterTestEntity.class));
    }


}
