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


    /**
     * <p>
     *     Converts a theme code to a {@link Theme} object. it´s case insensitive.
     * <p>
     * @param themeCode the theme code to be converted to a {@link Theme} object (e.g. "light")
     * @return the {@link Theme} object corresponding to the given theme code
     * @throws ThemeNotFoundException if the given theme code does not correspond to any {@link Theme} object
     * @see Theme#DEFAULT
     */
    public static Theme fromCode(String themeCode) {
        for (Theme theme : Theme.values()) {
            if (theme.getCode().equalsIgnoreCase(themeCode)) {
                return theme;
            }
        }
        throw new ThemeNotFoundException(themeCode);
    }

}
