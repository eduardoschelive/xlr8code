package com.xlr8code.server.article.service;

import com.xlr8code.server.article.repository.I18nArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class I18nArticleService {

    private final I18nArticleRepository i18nArticleRepository;

}
