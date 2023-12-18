package com.xlr8code.server.authentication.validator;

import com.xlr8code.server.authentication.annotation.UUIDToken;
import jakarta.annotation.Nullable;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UUIDTokenValidator implements ConstraintValidator<UUIDToken, String> {

    private static final String UUID_REGEX = "^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$";

    @Override
    public boolean isValid(@Nullable String value, ConstraintValidatorContext context) {
        return value != null && value.matches(UUID_REGEX);
    }

}
