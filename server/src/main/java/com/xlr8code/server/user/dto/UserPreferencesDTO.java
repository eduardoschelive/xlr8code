package com.xlr8code.server.user.dto;

import com.xlr8code.server.common.enums.Language;
import com.xlr8code.server.common.enums.Theme;
import com.xlr8code.server.user.entity.User;
import com.xlr8code.server.user.entity.UserPreferences;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "UserPreferences")
public record UserPreferencesDTO(
        @Schema(description = "The theme of the user.")
        Theme theme,
        @Schema(description = "The language of the user. The language is used for localization.")
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
