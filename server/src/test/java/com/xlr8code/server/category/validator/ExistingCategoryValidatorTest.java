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
    void it_should_return_true_when_category_exists() {
        var existingCategoryValidator = new ExistingCategoryValidator(categoryService);

        var categoryId = "categoryId";

        when(categoryService.existsById(categoryId)).thenReturn(true);

        assertTrue(existingCategoryValidator.isValid(categoryId, null));
    }

    @Test
    void it_should_return_false_when_category_does_not_exists() {
        var existingCategoryValidator = new ExistingCategoryValidator(categoryService);
        var categoryId = "";

        when(categoryService.existsById(categoryId)).thenReturn(false);

        assertFalse(existingCategoryValidator.isValid(categoryId, null));
    }

    @Test
    void it_should_return_true_when_category_is_optional_and_blank() {
        var existingCategoryValidator = new ExistingCategoryValidator(categoryService);
        var categoryId = "";
        existingCategoryValidator.optional = true;

        assertTrue(existingCategoryValidator.isValid(categoryId, null));
    }

    @Test
    void it_should_return_false_when_category_is_optional_and_not_blank() {
        var existingCategoryValidator = new ExistingCategoryValidator(categoryService);
        var categoryId = "categoryId";
        existingCategoryValidator.optional = true;

        when(categoryService.existsById(categoryId)).thenReturn(true);

        assertTrue(existingCategoryValidator.isValid(categoryId, null));
    }

}