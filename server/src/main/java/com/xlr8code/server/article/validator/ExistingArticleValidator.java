package com.xlr8code.server.article.validator;

import com.xlr8code.server.article.annotation.ExistingArticle;
import com.xlr8code.server.article.service.ArticleService;
import com.xlr8code.server.common.utils.StringUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ExistingArticleValidator implements ConstraintValidator<ExistingArticle, String> {

    private final ArticleService articleService;

    boolean optional;

    @Override
    public void initialize(ExistingArticle constraintAnnotation) {
        this.optional = constraintAnnotation.optional();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        if (isOptionalAndBlank(s)) {
            return true;
        }

        return this.articleService.existsById(s);
    }


    private boolean isOptionalAndBlank(String s) {
        return this.optional && StringUtils.isBlank(s);
    }

}
