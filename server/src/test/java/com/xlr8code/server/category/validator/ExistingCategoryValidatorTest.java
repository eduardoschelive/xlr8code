package com.xlr8code.server.category.validator;

import com.xlr8code.server.category.service.CategoryService;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ExistingCategoryValidatorTest {

    @MockBean
    private CategoryService categoryService;

    @Test
    void it_should_return_true_when_series_exists() {
        var existingSeriesValidator = new ExistingCategoryValidator(categoryService);

        var seriesId = "seriesId";

        when(categoryService.existsById(seriesId)).thenReturn(true);

        assertTrue(existingSeriesValidator.isValid(seriesId, null));
    }

    @Test
    void it_should_return_false_when_series_does_not_exists() {
        var existingSeriesValidator = new ExistingCategoryValidator(categoryService);
        var seriesId = "";

        when(categoryService.existsById(seriesId)).thenReturn(false);

        assertFalse(existingSeriesValidator.isValid(seriesId, null));
    }

    @Test
    void it_should_return_true_when_series_is_optional_and_blank() {
        var existingSeriesValidator = new ExistingCategoryValidator(categoryService);
        var seriesId = "";
        existingSeriesValidator.optional = true;

        assertTrue(existingSeriesValidator.isValid(seriesId, null));
    }

    @Test
    void it_should_return_false_when_series_is_optional_and_not_blank() {
        var existingSeriesValidator = new ExistingCategoryValidator(categoryService);
        var seriesId = "seriesId";
        existingSeriesValidator.optional = true;

        when(categoryService.existsById(seriesId)).thenReturn(true);

        assertTrue(existingSeriesValidator.isValid(seriesId, null));
    }

}