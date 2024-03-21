package com.xlr8code.server.common.validator;

import com.xlr8code.server.common.annotation.NullOrNotBlank;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NullOrNotBlankValidator implements ConstraintValidator<NullOrNotBlank, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return value == null || !value.isBlank();
    }
}