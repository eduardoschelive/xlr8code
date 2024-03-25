package com.xlr8code.server.article.service;

import com.xlr8code.server.article.dto.ArticleDTO;
import com.xlr8code.server.article.entity.Article;
import com.xlr8code.server.article.exception.ArticleNotFoundException;
import com.xlr8code.server.article.repository.ArticleRepository;
import com.xlr8code.server.common.utils.ObjectUtils;
import com.xlr8code.server.common.utils.UUIDUtils;
import com.xlr8code.server.series.exception.SeriesNotFoundException;
import com.xlr8code.server.series.service.SeriesService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final I18nArticleService i18nArticleService;
    private final SeriesService seriesService;

    @Lazy
    @Resource
    private ArticleService self;

    /**
     * @param articleDTO the article to be created
     * @return the created article
     */
    public Article create(ArticleDTO articleDTO) {
        this.i18nArticleService.validateSlugInList(articleDTO.languages().values());
        var article = convertToEntity(articleDTO);
        return this.articleRepository.save(article);
    }

    /**
     * @param id the id of the article
     * @return true if an article with the specified id exists, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean existsById(String id) {
        var uuid = UUIDUtils.convertFromString(id);
        return uuid.filter(this.articleRepository::existsById).isPresent();
    }

    /**
     * @param id the id of the article
     * @return the article with the specified id
     * @throws ArticleNotFoundException if the article with the specified id does not exist
     */
    @Transactional(readOnly = true)
    public Article findById(String id) {
        var uuid = UUIDUtils.convertFromString(id).orElseThrow(() -> new ArticleNotFoundException(id));
        return self.findById(uuid);
    }

    /**
     * @param uuid the id of the article
     * @return the article with the specified id
     * @throws ArticleNotFoundException if the article with the specified id does not exist
     */
    @Transactional(readOnly = true)
    public Article findById(UUID uuid) {
        return this.articleRepository.findById(uuid).orElseThrow(() -> new ArticleNotFoundException(uuid.toString()));
    }

    /**
     * @param id the id of the article
     */
    @Transactional
    public void delete(String id) {
        var uuid = UUIDUtils.convertFromString(id).orElseThrow(() -> new ArticleNotFoundException(id));
        this.articleRepository.deleteById(uuid);
    }

    @Transactional
    public Article update(String id, ArticleDTO articleDTO) {
        var article = self.findById(id);

        this.i18nArticleService.validateSlugInList(articleDTO.languages().values(), article);

        var updatedArticle = convertToEntity(articleDTO);
        updatedArticle.setId(article.getId());

        // set id for the i18n articles
        updatedArticle.getI18nArticles().forEach(i18nArticle -> {
            i18nArticle.setArticle(updatedArticle);
            i18nArticle.setId(article.getI18nArticles().stream()
                    .filter(i18nArticle1 -> i18nArticle1.getLanguage().equals(i18nArticle.getLanguage()))
                    .findFirst()
                    .orElse(i18nArticle)
                    .getId());
        });

        return this.articleRepository.save(updatedArticle);
    }

    /**
     * @param articleDTO the article to be converted
     * @return the converted article
     * @throws ArticleNotFoundException if the article with the specified id does not exist
     * @throws SeriesNotFoundException  if the series with the specified id does not exist
     */
    private Article convertToEntity(ArticleDTO articleDTO) {
        var series = ObjectUtils.executeIfNotNull(articleDTO.seriesId(), seriesService::findById);
        var nextArticle = ObjectUtils.executeIfNotNull(articleDTO.nextArticleId(), self::findById);
        var previousArticle = ObjectUtils.executeIfNotNull(articleDTO.previousArticleId(), self::findById);
        var parentArticle = ObjectUtils.executeIfNotNull(articleDTO.parentArticleId(), self::findById);

        return articleDTO.toEntity(series, nextArticle, previousArticle, parentArticle);
    }

}
