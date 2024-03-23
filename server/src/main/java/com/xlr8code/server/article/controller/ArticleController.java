package com.xlr8code.server.article.controller;

import com.xlr8code.server.article.dto.ArticleDTO;
import com.xlr8code.server.article.service.ArticleService;
import com.xlr8code.server.common.utils.Endpoint;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping(Endpoint.Article.BASE_PATH)
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody  ArticleDTO articleDTO) {
        var newArticle = this.articleService.create(articleDTO);
        var createdURI = Endpoint.Article.BASE_PATH + "/" + newArticle.getId();

        return ResponseEntity.created(URI.create(createdURI)).build();
    }

}
