package com.xlr8code.server.filter.strategies;

import com.xlr8code.server.common.enums.Theme;
import com.xlr8code.server.filter.entity.FilterTestEntity;
import com.xlr8code.server.filter.repository.FilterTestRepository;
import com.xlr8code.server.filter.utils.FilterTestUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class EnumParsingStrategyTest {

    private static final Theme THEME = Theme.DARK;

    @Autowired
    private FilterTestRepository testRepository;

    @Autowired
    private FilterTestUtils filterTestUtils;

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
        var params = Map.of(
                "enumThemeField_eq", THEME.toString()
        );

        var spec = filterTestUtils.buildSpecification(params);
        var pageable = Pageable.unpaged();

        var results = testRepository.findAll(spec, pageable);
        assertEquals(1, results.getTotalElements());
    }


}