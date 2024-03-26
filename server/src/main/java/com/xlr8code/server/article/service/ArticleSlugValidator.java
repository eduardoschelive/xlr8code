package com.xlr8code.server.article.service;

import com.xlr8code.server.article.dto.ArticleLanguageDTO;
import com.xlr8code.server.article.entity.Article;
import com.xlr8code.server.article.repository.I18nArticleRepository;
import com.xlr8code.server.common.validation.SlugValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ArticleSlugValidator implements SlugValidator<Article> {

    private final I18nArticleRepository i18nArticleRepository;


    @Override
    public boolean existsBySlug(String slug) {
        return i18nArticleRepository.existsBySlug(slug);
    }

    @Override
    public boolean existsBySlugAndOwnerNot(String slug, Article ownerEntity) {
        return i18nArticleRepository.existsBySlugAndArticleNot(slug, ownerEntity);
    }

    public void validateSlugs(Collection<ArticleLanguageDTO> articleLanguageDTOs) {
        var slugs = articleLanguageDTOs.stream().map(ArticleLanguageDTO::slug).toList();
        this.validateSlugInList(slugs);
    }

    public void validateSlugs(Collection<ArticleLanguageDTO> articleLanguageDTOs, Article article) {
        var slugs = articleLanguageDTOs.stream().map(ArticleLanguageDTO::slug).toList();
        this.validateSlugInList(slugs, article);
    }

}
