package com.xlr8code.server.common.utils;

import com.xlr8code.server.common.exception.ApplicationException;
import com.xlr8code.server.common.exception.CommonExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Theme {

    SYSTEM("system"),
    LIGHT("light"),
    DARK("dark");

    private static final Theme DEFAULT = SYSTEM;
    private final String code;

    public static Theme fromCode(String themeCode) {
        for (Theme theme : Theme.values()) {
            if (theme.getCode().equals(themeCode)) {
                return theme;
            }
        }
        throw new ApplicationException(CommonExceptionType.THEME_NOT_FOUND, themeCode);
    }

}
