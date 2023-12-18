package com.xlr8code.server.user.utils;

import com.xlr8code.server.common.exception.ApplicationException;
import com.xlr8code.server.user.exception.UserExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Language {

    ENGLISH("en-US"),
    BRAZILIAN_PORTUGUESE("pt_BR");

    private final String code;

    private static final Language DEFAULT = ENGLISH;

    public static Language fromCode(String languageCode) throws ApplicationException {
        for (Language language : Language.values()) {
            if (language.getCode().equals(languageCode)) {
                return language;
            }
        }
        throw new ApplicationException(UserExceptionType.LANGUAGE_NOT_FOUND, languageCode);
    }

}
