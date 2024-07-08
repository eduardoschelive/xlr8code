package com.xlr8code.server.article.controller;

import com.xlr8code.server.article.dto.ArticleDTO;
import com.xlr8code.server.article.dto.TranslatedArticleDTO;
import com.xlr8code.server.article.service.ArticleService;
import com.xlr8code.server.common.annotation.MultiLanguageContent;
import com.xlr8code.server.common.service.LocaleService;
import com.xlr8code.server.common.utils.Endpoint;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping(Endpoint.Article.BASE_PATH)
public class ArticleController {

    private final ArticleService articleService;
    private final LocaleService localeService;

    @PostMapping
    @PreAuthorize("@articleSecurityService.canModifyResource(principal)")
    public ResponseEntity<Void> create(@Valid @RequestBody ArticleDTO articleDTO) {
        var newArticle = this.articleService.create(articleDTO);
        var createdURI = Endpoint.Article.BASE_PATH + "/" + newArticle.getId();

        return ResponseEntity.created(URI.create(createdURI)).build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@articleSecurityService.canModifyResource(principal)")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        this.articleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("@articleSecurityService.canModifyResource(principal)")
    public ResponseEntity<Void> update(@PathVariable String id, @Valid @RequestBody ArticleDTO articleDTO) {
        this.articleService.update(id, articleDTO);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @MultiLanguageContent
    public ResponseEntity<TranslatedArticleDTO> get(@PathVariable String id, HttpServletRequest request) {
        var languages = this.localeService.getAllAcceptedLanguages(request);
        var result = this.articleService.findById(id, languages);
        return ResponseEntity.ok(result);
    }

}
