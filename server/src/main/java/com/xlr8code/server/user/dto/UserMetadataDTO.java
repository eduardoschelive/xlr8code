package com.xlr8code.server.user.dto;

import com.xlr8code.server.common.utils.Language;
import com.xlr8code.server.common.utils.Theme;
import com.xlr8code.server.user.entity.UserMetadata;

public record UserMetadataDTO(
        Theme themePreference,
        Language languagePreference,
        String profilePictureUrl
) {
    public static UserMetadataDTO fromUserMetadata(UserMetadata userMetadata) {
        return new UserMetadataDTO(
                userMetadata.getThemePreference(),
                userMetadata.getLanguagePreference(),
                userMetadata.getProfilePictureUrl()
        );
    }

}
