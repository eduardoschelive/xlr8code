package com.xlr8code.server.common.utils;

import com.xlr8code.server.common.exception.ApplicationException;
import com.xlr8code.server.common.exception.CommonExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Language {

    ENGLISH("en_US"),
    BRAZILIAN_PORTUGUESE("pt_BR");

    private static final Language DEFAULT = ENGLISH;
    private final String code;

    public static Language fromCode(String languageCode) {
        for (Language language : Language.values()) {
            if (language.getCode().equals(languageCode)) {
                return language;
            }
        }
        throw new ApplicationException(CommonExceptionType.LANGUAGE_NOT_FOUND, languageCode);
    }

}
