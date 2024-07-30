package com.xlr8code.server.article.exception;

import com.xlr8code.server.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;


public class ArticleNotFoundException extends ApplicationException {

    public ArticleNotFoundException(String articleId) {
        super("The article with the specified id was not found", articleId);
    }

    @Override
    public String getMessageIdentifier() {
        return "article.error.not_found";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public String getErrorCode() {
        return "ARTICLE_NOT_FOUND";
    }

}
