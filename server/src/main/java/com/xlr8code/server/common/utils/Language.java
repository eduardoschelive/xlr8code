package com.xlr8code.server.common.utils;

import com.xlr8code.server.user.exception.LanguageNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Language {

    AMERICAN_ENGLISH("en_US"),
    BRAZILIAN_PORTUGUESE("pt_BR");

    private static final Language DEFAULT = AMERICAN_ENGLISH;
    private final String code;

    public static Language fromCode(String languageCode) {
        for (Language language : Language.values()) {
            if (language.getCode().equals(languageCode)) {
                return language;
            }
        }
        throw new LanguageNotFoundException(languageCode);
    }

}
