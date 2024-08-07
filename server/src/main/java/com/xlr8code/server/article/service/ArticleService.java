package com.xlr8code.server.article.service;

import com.xlr8code.server.article.dto.ArticleDTO;
import com.xlr8code.server.article.dto.TranslatedArticleDTO;
import com.xlr8code.server.article.entity.Article;
import com.xlr8code.server.article.entity.ArticleRelation;
import com.xlr8code.server.article.exception.ArticleNotFoundException;
import com.xlr8code.server.article.repository.ArticleRepository;
import com.xlr8code.server.category.exception.CategoryNotFoundException;
import com.xlr8code.server.category.service.CategoryService;
import com.xlr8code.server.common.enums.Language;
import com.xlr8code.server.common.exception.DuplicateSlugInLanguagesException;
import com.xlr8code.server.common.exception.SlugAlreadyExistsException;
import com.xlr8code.server.common.utils.ObjectUtils;
import com.xlr8code.server.common.utils.UUIDUtils;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleSlugValidator articleSlugValidator;
    private final CategoryService categoryService;

    @Lazy
    @Resource
    private ArticleService self;

    /**
     * @param articleDTO the article to be created
     * @return the created article
     */
    public Article create(ArticleDTO articleDTO) {
        this.articleSlugValidator.validateSlugs(articleDTO.languages().values());
        var emptyArticle = new Article();
        var article = convertToEntity(emptyArticle, articleDTO);
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
     * @param id        the id of the article
     * @param languages the languages to be used for the translation
     * @return the article with the specified id
     */
    @Transactional(readOnly = true)
    public TranslatedArticleDTO findById(String id, Set<Language> languages) {
        var article = self.findById(id);
        return TranslatedArticleDTO.fromEntity(article, languages);
    }

    /**
     * @param specification the specification to filter for the articles
     * @param pageable      the pageable to filter
     * @param languages     the languages to filter
     * @return the articles with the specified languages and filters
     */
    @Transactional(readOnly = true)
    public Page<TranslatedArticleDTO> findAll(Specification<Article> specification, Pageable pageable, Set<Language> languages) {
        var page = this.articleRepository.findAll(specification, pageable);
        return page.map(article -> TranslatedArticleDTO.fromEntity(article, languages));
    }

    /**
     * @param id the id of the article
     */
    @Transactional
    public void delete(String id) {
        var uuid = UUIDUtils.convertFromString(id).orElseThrow(() -> new ArticleNotFoundException(id));
        this.articleRepository.deleteById(uuid);
    }

    /**
     * @param id         the id of the article
     * @param articleDTO the article to be updated
     * @return the updated article
     * @throws ArticleNotFoundException          if the article with the specified id does not exist
     * @throws CategoryNotFoundException         if the category with the specified id does not exist
     * @throws DuplicateSlugInLanguagesException if the slug is duplicated in the languages
     * @throws SlugAlreadyExistsException        if the slug already exists
     */
    @Transactional
    public TranslatedArticleDTO update(String id, ArticleDTO articleDTO) {
        var articleToUpdate = self.findById(id);

        this.articleSlugValidator.validateSlugs(articleDTO.languages().values(), articleToUpdate);

        var articleToUpdateEntity = convertToEntity(articleToUpdate, articleDTO);

        // set id for the i18n articles
        articleToUpdateEntity.getI18nArticles().forEach(i18nArticle -> {
            i18nArticle.setArticle(articleToUpdateEntity);
            i18nArticle.setId(articleToUpdate.getI18nArticles().stream()
                    .filter(i18nArticle1 -> i18nArticle1.getLanguage().equals(i18nArticle.getLanguage()))
                    .findFirst()
                    .orElse(i18nArticle)
                    .getId());
        });

        var updatedArticle = this.articleRepository.save(articleToUpdateEntity);

        return TranslatedArticleDTO.fromEntity(updatedArticle, articleDTO.languages().keySet());
    }

    /**
     * @param articleDTO the article to be converted
     * @return the converted article
     * @throws ArticleNotFoundException  if the article with the specified id does not exist
     * @throws CategoryNotFoundException if the category with the specified id does not exist
     */
    private Article convertToEntity(Article article, ArticleDTO articleDTO) {
        var category = ObjectUtils.executeIfNotNull(articleDTO.categoryId(), categoryService::findById);
        var nextArticle = ObjectUtils.executeIfNotNull(articleDTO.nextArticleId(), self::findById);
        var previousArticle = ObjectUtils.executeIfNotNull(articleDTO.previousArticleId(), self::findById);

        var articleRelation = ArticleRelation.builder()
                .nextArticle(nextArticle)
                .previousArticle(previousArticle)
                .build();

        return articleDTO.toEntity(article, category, articleRelation);
    }

}
