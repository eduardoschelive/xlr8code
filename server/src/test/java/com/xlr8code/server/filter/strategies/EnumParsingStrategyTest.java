package com.xlr8code.server.filter.strategies;

import com.xlr8code.server.common.enums.Theme;
import com.xlr8code.server.filter.entity.FilterTestEntity;
import com.xlr8code.server.filter.repository.FilterTestRepository;
import com.xlr8code.server.filter.utils.FilterTestUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class EnumParsingStrategyTest {

    private static final Theme THEME = Theme.DARK;

    @Autowired
    private FilterTestRepository testRepository;

    @BeforeAll
    static void setup(@Autowired FilterTestRepository repository) {
        var entity = FilterTestEntity.builder()
                .stringField("stringField")
                .booleanField(true)
                .enumThemeField(THEME)
                .build();

        repository.save(entity);
    }

    @AfterAll
    static void cleanup(@Autowired FilterTestUtils filterTestUtils) {
        filterTestUtils.clearRepositories();
    }

    @Test
    void it_should_filter_with_enum_field() {
        var results = testRepository.findAll(Map.of("enumThemeField_eq", THEME.toString()), FilterTestEntity.class);
        assertEquals(1, results.getTotalElements());
    }



}