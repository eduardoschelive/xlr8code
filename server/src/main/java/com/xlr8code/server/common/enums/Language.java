package com.xlr8code.server.common.enums;

import com.xlr8code.server.user.exception.LanguageNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
@Getter
public enum Language {

    AMERICAN_ENGLISH("en_US"),
    BRAZILIAN_PORTUGUESE("pt_BR");

    public static final Map<String, String> VARIANT_MAP = Map.of(
            "en", AMERICAN_ENGLISH.getCode(),
            "pt", BRAZILIAN_PORTUGUESE.getCode()
    );
    private static final Language DEFAULT = AMERICAN_ENGLISH;
    private final String code;

    /**
     * <p>
     * Converts a language code to a {@link Language} object. it´s case insensitive.
     * </p>
     *
     * @param languageCode the language code to be converted to a {@link Language} object (e.g. "pt-BR")
     * @return the {@link Language} object corresponding to the given language code
     * @throws LanguageNotFoundException if the given language code does not correspond to any {@link Language} object
     * @see Language#DEFAULT
     */
    public static Language fromCode(String languageCode) {
        for (Language language : Language.values()) {
            if (language.getCode().equalsIgnoreCase(languageCode)) {
                return language;
            }
        }
        throw new LanguageNotFoundException(languageCode);
    }

}
