package com.xlr8code.server.article.validator;

import com.xlr8code.server.article.service.ArticleService;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ExistingArticleValidatorTest {

    @MockBean
    private ArticleService articleService;

    @Test
    void it_should_return_true_when_article_exists() {
        var existingArticleValidator = new ExistingArticleValidator(articleService);

        var articleId = "articleId";

        when(articleService.existsById(articleId)).thenReturn(true);

        assertTrue(existingArticleValidator.isValid(articleId, null));
    }

    @Test
    void it_should_return_false_when_article_does_not_exists() {
        var existingArticleValidator = new ExistingArticleValidator(articleService);
        var articleId = "";

        when(articleService.existsById(articleId)).thenReturn(false);

        assertFalse(existingArticleValidator.isValid(articleId, null));
    }

    @Test
    void it_should_return_true_when_article_is_optional_and_blank() {
        var existingArticleValidator = new ExistingArticleValidator(articleService);
        var articleId = "";
        existingArticleValidator.optional = true;

        assertTrue(existingArticleValidator.isValid(articleId, null));
    }

    @Test
    void it_should_return_false_when_article_is_optional_and_not_blank() {
        var existingArticleValidator = new ExistingArticleValidator(articleService);
        var articleId = "articleId";
        existingArticleValidator.optional = true;

        when(articleService.existsById(articleId)).thenReturn(true);

        assertTrue(existingArticleValidator.isValid(articleId, null));
    }

}