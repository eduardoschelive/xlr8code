package com.xlr8code.server.article.service;

import com.xlr8code.server.article.dto.ArticleLanguageDTO;
import com.xlr8code.server.article.repository.I18nArticleRepository;
import com.xlr8code.server.common.exception.DuplicateSlugInLanguagesException;
import com.xlr8code.server.common.exception.SlugAlreadyExistsException;
import com.xlr8code.server.series.dto.SeriesLanguageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class I18nArticleService {

    private final I18nArticleRepository i18nArticleRepository;

    /**
     * @param slug the slug to be validated
     */
    private void validateSlug(String slug) {
        if (i18nArticleRepository.existsBySlug(slug)) {
            throw new SlugAlreadyExistsException(slug);
        }
    }

    /**
     * @param slugs the slugs to be validated
     */
    private void validateSlugInCollection(Collection<String> slugs) {
        slugs.forEach(this::validateSlug);
    }

    /**
     * @param languages the languages to be validated
     * @throws DuplicateSlugInLanguagesException if the slugs are duplicated
     */
    private void validateDuplicateSlugs(Collection<ArticleLanguageDTO> languages) {
        Set<String> slugSet = new HashSet<>();
        for (var language : languages) {
            String slug = language.slug();
            if (!slugSet.add(slug)) {
                throw new DuplicateSlugInLanguagesException(slug);
            }
        }
    }

    /**
     * @param articleLanguageDTOS the i18n article to be validated
     * @throws SlugAlreadyExistsException        if the slug already exists
     * @throws DuplicateSlugInLanguagesException if the slugs are duplicated
     */
    @Transactional(readOnly = true)
    public void validateSlugInList(Collection<ArticleLanguageDTO> articleLanguageDTOS) {
        this.validateDuplicateSlugs(articleLanguageDTOS);
        List<String> slugs = articleLanguageDTOS.stream()
                .map(ArticleLanguageDTO::slug)
                .toList();
        validateSlugInCollection(slugs);
    }

}
