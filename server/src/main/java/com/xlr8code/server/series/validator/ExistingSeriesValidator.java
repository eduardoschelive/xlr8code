package com.xlr8code.server.series.validator;

import com.xlr8code.server.common.utils.StringUtils;
import com.xlr8code.server.series.annotation.ExistingSeries;
import com.xlr8code.server.series.service.SeriesService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ExistingSeriesValidator implements ConstraintValidator<ExistingSeries, String> {

    private final SeriesService seriesService;

    boolean optional;

    @Override
    public void initialize(ExistingSeries constraintAnnotation) {
        this.optional = constraintAnnotation.optional();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        if (isOptionalAndBlank(s)) {
            return true;
        }

        return this.seriesService.existsById(s);
    }


    private boolean isOptionalAndBlank(String s) {
        return this.optional && StringUtils.isBlank(s);
    }

}
