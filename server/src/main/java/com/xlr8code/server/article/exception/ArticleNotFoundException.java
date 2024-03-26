package com.xlr8code.server.article.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;


public class ArticleNotFoundException extends ApplicationException {

    public ArticleNotFoundException(String articleId) {
        super("ARTICLE_NOT_FOUND", articleId);
    }

    @Override
    public String getMessageIdentifier() {
        return "article.error.not_found";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
