package com.xlr8code.server.user.utils;

import com.xlr8code.server.common.exception.ApplicationException;
import com.xlr8code.server.user.exception.UserExceptionType;
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
        throw new ApplicationException(UserExceptionType.THEME_NOT_FOUND, themeCode);
    }

}
