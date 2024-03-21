package com.xlr8code.server.article.service;

import com.xlr8code.server.article.dto.ArticleDTO;
import com.xlr8code.server.article.entity.Article;
import com.xlr8code.server.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    public Article create(ArticleDTO articleDTO) {
        var article = articleDTO.toEntity();
        return this.articleRepository.save(article);
    }

}
