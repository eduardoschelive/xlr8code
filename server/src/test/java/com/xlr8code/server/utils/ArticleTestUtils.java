package com.xlr8code.server.utils;

import com.xlr8code.server.article.dto.ArticleDTO;
import com.xlr8code.server.article.dto.ArticleLanguageDTO;
import com.xlr8code.server.common.enums.Language;

import java.util.Map;
import java.util.UUID;

public class ArticleTestUtils {

    public static ArticleDTO buildArticleDTO(String seriesId, String previousArticleId, String nextArticleId, String parentArticleId, Integer position, Map<Language, ArticleLanguageDTO> languages) {
        return new ArticleDTO(seriesId, previousArticleId, nextArticleId, parentArticleId, position, languages);
    }

    public static ArticleDTO buildArticleDTO(String seriesId, String previousArticleId, String nextArticleId, String parentArticleId, Integer position) {
        var languages = Map.of(
                Language.AMERICAN_ENGLISH, buildArticleLanguageDTO("title", UUID.randomUUID().toString(), "content")
        );

        return buildArticleDTO(seriesId, previousArticleId, nextArticleId, parentArticleId, position, languages);
    }

    public static ArticleDTO buildArticleDTO() {
        return buildArticleDTO(null, null, null, null, 1);
    }

    public static ArticleDTO buildArticleDTOWithParentArticle(String parentArticleId) {
        return buildArticleDTO(null, null, null, parentArticleId, 1);
    }

    public static ArticleLanguageDTO buildArticleLanguageDTO(String title, String slug, String content) {
        return new ArticleLanguageDTO(title, slug, content);
    }

    public static ArticleDTO buildArticleDTOWithNextAndPreviousArticle(String nextArticleId, String previousArticleId) {
        return buildArticleDTO(null, previousArticleId, nextArticleId, null, 1);
    }

    public static ArticleDTO buildArticleDTOWithSeries(String string) {
        return buildArticleDTO(string, null, null, null, 1);
    }

}
