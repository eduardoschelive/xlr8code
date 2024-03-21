package com.xlr8code.server.article.controller;

import com.xlr8code.server.article.dto.ArticleDTO;
import com.xlr8code.server.common.utils.Endpoint;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Endpoint.Article.BASE_PATH)
public class ArticleController {

    @PostMapping
    public void create(@Valid @RequestBody  ArticleDTO articleDTO) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

}
