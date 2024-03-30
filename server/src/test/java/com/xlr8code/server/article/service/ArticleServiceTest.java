package com.xlr8code.server.article.service;

import com.xlr8code.server.article.entity.Article;
import com.xlr8code.server.article.exception.ArticleNotFoundException;
import com.xlr8code.server.article.repository.ArticleRepository;
import com.xlr8code.server.series.entity.Series;
import com.xlr8code.server.series.repository.SeriesRepository;
import com.xlr8code.server.series.service.SeriesService;
import com.xlr8code.server.utils.ArticleTestUtils;
import com.xlr8code.server.utils.SeriesTestUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ArticleServiceTest {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private SeriesService seriesService;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private SeriesRepository seriesRepository;

    private Series series;
    private Article parentArticle;
    private Article nextArticle;
    private Article previousArticle;


    @BeforeEach
    void setUp() {
        series = seriesService.create(SeriesTestUtils.buildSeriesDTO());
        parentArticle = articleService.create(ArticleTestUtils.buildArticleDTO());
        nextArticle = articleService.create(ArticleTestUtils.buildArticleDTO());
        previousArticle = articleService.create(ArticleTestUtils.buildArticleDTO());
    }

    @AfterEach
    void tearDown() {
        articleRepository.deleteAll();
        seriesRepository.deleteAll();
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
        void it_should_create_article_with_parent() {
            var articleDTO = ArticleTestUtils.buildArticleDTOWithParentArticle(parentArticle.getId().toString());

            var article = articleService.create(articleDTO);

            assertNotNull(article);
            assertEquals(parentArticle.getId(), article.getArticleRelation().getParentArticle().getId());
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
        void it_should_create_article_with_series() {
            var articleDTO = ArticleTestUtils.buildArticleDTOWithSeries(series.getId().toString());

            var article = articleService.create(articleDTO);

            assertNotNull(article);
            assertEquals(series.getId(), article.getSeries().getId());
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
            assertEquals(article.getId(), updatedArticle.getId());
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

    }

}