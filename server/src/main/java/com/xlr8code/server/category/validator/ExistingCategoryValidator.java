package com.xlr8code.server.category.validator;

import com.xlr8code.server.category.annotation.ExistingCategory;
import com.xlr8code.server.category.service.CategoryService;
import com.xlr8code.server.common.utils.StringUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ExistingCategoryValidator implements ConstraintValidator<ExistingCategory, String> {

    private final CategoryService categoryService;

    boolean optional;

    @Override
    public void initialize(ExistingCategory constraintAnnotation) {
        this.optional = constraintAnnotation.optional();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        if (isOptionalAndBlank(s)) {
            return true;
        }

        return this.categoryService.existsById(s);
    }


    private boolean isOptionalAndBlank(String s) {
        return this.optional && StringUtils.isBlank(s);
    }

}
