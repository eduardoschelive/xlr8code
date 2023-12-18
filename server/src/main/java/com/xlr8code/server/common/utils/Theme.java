package com.xlr8code.server.common.utils;

import com.xlr8code.server.user.exception.ThemeNotFoundException;
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
        throw new ThemeNotFoundException(themeCode);
    }

}
