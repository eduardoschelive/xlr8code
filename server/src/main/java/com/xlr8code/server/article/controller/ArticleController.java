package com.xlr8code.server.article.controller;

import com.xlr8code.server.article.dto.ArticleDTO;
import com.xlr8code.server.article.dto.TranslatedArticleDTO;
import com.xlr8code.server.article.entity.Article;
import com.xlr8code.server.article.exception.ArticleNotFoundException;
import com.xlr8code.server.article.service.ArticleService;
import com.xlr8code.server.category.exception.CategoryNotFoundException;
import com.xlr8code.server.common.annotation.MultiLanguageContent;
import com.xlr8code.server.common.exception.DuplicateSlugInLanguagesException;
import com.xlr8code.server.common.exception.SlugAlreadyExistsException;
import com.xlr8code.server.common.service.LocaleService;
import com.xlr8code.server.common.utils.Endpoint;
import com.xlr8code.server.filter.annotation.FilterEndpoint;
import com.xlr8code.server.filter.utils.FilterUtils;
import com.xlr8code.server.openapi.annotation.ErrorResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping(Endpoint.Article.BASE_PATH)
@Tag(name = "Article")
public class ArticleController {

    private final ArticleService articleService;
    private final LocaleService localeService;

    @Operation(
            summary = "Create article",
            description = "Use this endpoint to create a new article.",
            responses = @ApiResponse(responseCode = "201")
    )
    @ErrorResponses(value = {
            DuplicateSlugInLanguagesException.class,
            SlugAlreadyExistsException.class,
            CategoryNotFoundException.class
    })
    @PostMapping
    public ResponseEntity<Void> createArticle(@Valid @RequestBody ArticleDTO articleDTO) {
        var newArticle = this.articleService.create(articleDTO);
        var createdURI = Endpoint.Article.BASE_PATH + "/" + newArticle.getId();

        return ResponseEntity.created(URI.create(createdURI)).build();
    }

    @Operation(
            summary = "Delete article",
            description = "Use this endpoint to delete an article by its unique identifier.",
            responses = @ApiResponse(responseCode = "200")
    )
    @ErrorResponses(value = ArticleNotFoundException.class)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@Schema(description = "The article unique identifier") @PathVariable String id) {
        this.articleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Update article",
            description = "Use this endpoint to update an article by its unique identifier.",
            responses = @ApiResponse(responseCode = "200")
    )
    @ErrorResponses(value = {
            ArticleNotFoundException.class,
            SlugAlreadyExistsException.class,
            DuplicateSlugInLanguagesException.class,
            CategoryNotFoundException.class
    })
    @PutMapping("/{id}")
    public ResponseEntity<TranslatedArticleDTO> update(
            @Schema(description = "The article unique identifier") @PathVariable String id,
            @Valid @RequestBody ArticleDTO articleDTO
    ) {
        var updatedArticle = this.articleService.update(id, articleDTO);
        return ResponseEntity.ok(updatedArticle);
    }


    @Operation(
            summary = "Find article",
            description = "Use this endpoint to find an article by its unique identifier.",
            responses = @ApiResponse(responseCode = "200")
    )
    @ErrorResponses(value = ArticleNotFoundException.class)
    @GetMapping("/{id}")
    @MultiLanguageContent
    public ResponseEntity<TranslatedArticleDTO> findArticle(
            @Schema(description = "The article unique identifier") @PathVariable String id,
            HttpServletRequest request
    ) {
        var languages = this.localeService.getAllAcceptedLanguages(request);
        var result = this.articleService.findById(id, languages);
        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "List articles",
            description = "Use this endpoint to list articles and filter articles by their fields.",
            responses = @ApiResponse(responseCode = "200")
    )
    @GetMapping
    @MultiLanguageContent
    @FilterEndpoint(Article.class)
    public ResponseEntity<PagedModel<TranslatedArticleDTO>> listArticles(Specification<Article> specification, Pageable pageable, HttpServletRequest request) {
        var languages = this.localeService.getAllAcceptedLanguages(request);
        var result = this.articleService.findAll(specification, pageable, languages);
        return FilterUtils.buildResponseEntity(new PagedModel<>(result));
    }

}
