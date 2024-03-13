package com.xlr8code.server.user.dto;

import com.xlr8code.server.common.enums.Language;
import com.xlr8code.server.common.enums.Theme;
import com.xlr8code.server.user.entity.User;
import com.xlr8code.server.user.entity.UserPreferences;

public record UserPreferencesDTO(
        Theme theme,
        Language language
) {

    public static UserPreferencesDTO from(UserPreferences userPreferences) {
        return new UserPreferencesDTO(
                userPreferences.getTheme(),
                userPreferences.getLanguage()
        );
    }

    public UserPreferences toUserPreferences(User user) {
        return UserPreferences.builder()
                .user(user)
                .theme(this.theme())
                .language(this.language())
                .build();
    }

}
