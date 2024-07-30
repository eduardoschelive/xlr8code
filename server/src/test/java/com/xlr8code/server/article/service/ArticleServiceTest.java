package com.xlr8code.server.article.service;

import com.xlr8code.server.article.entity.Article;
import com.xlr8code.server.article.exception.ArticleNotFoundException;
import com.xlr8code.server.article.repository.ArticleRepository;
import com.xlr8code.server.category.entity.Category;
import com.xlr8code.server.category.repository.CategoryRepository;
import com.xlr8code.server.category.service.CategoryService;
import com.xlr8code.server.common.enums.Language;
import com.xlr8code.server.utils.ArticleTestUtils;
import com.xlr8code.server.utils.CategoryTestUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ArticleServiceTest {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category category;
    private Article nextArticle;
    private Article previousArticle;


    @BeforeEach
    void setUp() {
        category = categoryService.create(CategoryTestUtils.buildCategoryDTO());
        nextArticle = articleService.create(ArticleTestUtils.buildArticleDTO());
        previousArticle = articleService.create(ArticleTestUtils.buildArticleDTO());
    }

    @AfterEach
    void tearDown() {
        articleRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Nested
    class CreateTests {

        @Test
        void it_should_create_article() {
            var articleDTO = ArticleTestUtils.buildArticleDTO();

            var article = articleService.create(articleDTO);

            assertNotNull(article);
        }


        @Test
        void it_should_create_article_with_next_and_previous_article() {
            var articleDTO = ArticleTestUtils.buildArticleDTOWithNextAndPreviousArticle(nextArticle.getId().toString(), previousArticle.getId().toString());

            var article = articleService.create(articleDTO);

            assertNotNull(article);
            assertEquals(nextArticle.getId(), article.getArticleRelation().getNextArticle().getId());
            assertEquals(previousArticle.getId(), article.getArticleRelation().getPreviousArticle().getId());
        }

        @Test
        void it_should_create_article_with_category() {
            var articleDTO = ArticleTestUtils.buildArticleDTOWithCategory(category.getId().toString());

            var article = articleService.create(articleDTO);

            assertNotNull(article);
            assertEquals(category.getId(), article.getCategory().getId());
        }

    }

    @Nested
    class UpdateTests {

        @Test
        void it_should_update_article() {
            var article = articleRepository.findAll().getFirst();
            var articleDTO = ArticleTestUtils.buildArticleDTO();

            var updatedArticle = articleService.update(article.getId().toString(), articleDTO);

            assertNotNull(updatedArticle);
            assertEquals(article.getId(), updatedArticle.id());
        }

        @Test
        void it_should_throw_exception_when_updating_non_existing_article() {
            var articleDTO = ArticleTestUtils.buildArticleDTO();

            assertThrows(ArticleNotFoundException.class, () -> articleService.update("non-existing-id", articleDTO));
        }

    }

    @Nested
    class DeleteTests {

        @Test
        void it_should_delete_article() {
            var article = articleRepository.findAll().getFirst();

            articleService.delete(article.getId().toString());
            var result = articleRepository.findById(article.getId());

            assertTrue(result.isEmpty());
        }

        @Test
        void it_should_throw_exception_when_deleting_non_existing_article() {
            assertThrows(ArticleNotFoundException.class, () -> articleService.delete("non-existing-id"));
        }

    }

    @Nested
    class FindTests {

        @Test
        void it_should_return_if_exists_by_id() {
            var article = articleRepository.findAll().getFirst();

            var result = articleService.existsById(article.getId().toString());

            assertTrue(result);
        }

        @Test
        void it_should_return_article() {
            var article = articleRepository.findAll().getFirst();

            var result = articleService.findById(article.getId().toString());

            assertNotNull(result);
            assertEquals(article.getId(), result.getId());
        }

        @Test
        void it_should_throw_exception_when_finding_non_existing_article() {
            assertThrows(ArticleNotFoundException.class, () -> articleService.findById("non-existing-id"));
        }

        @Test
        void it_should_return_translated_article() {
            var article = articleRepository.findAll().getFirst();

            var result = articleService.findById(article.getId().toString(), Set.of(Language.AMERICAN_ENGLISH));

            assertNotNull(result);
            assertEquals(article.getId(), result.id());
        }

        @Test
        void it_should_return_all_articles() {
            var result = articleService.findAll(Specification.where(null), Pageable.unpaged(), Set.of(Language.AMERICAN_ENGLISH));

            assertFalse(result.isEmpty());
        }

    }

}