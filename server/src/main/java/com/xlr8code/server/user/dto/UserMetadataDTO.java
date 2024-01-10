package com.xlr8code.server.user.dto;

import com.xlr8code.server.common.utils.Language;
import com.xlr8code.server.common.utils.Theme;

public record UserMetadataDTO(
        Theme themePreference,
        Language languagePreference,
        String profilePictureUrl
) {
    public static UserMetadataDTO fromUserMetadata(Theme themePreference, Language languagePreference, String profilePictureUrl) {
        return new UserMetadataDTO(
                themePreference,
                languagePreference,
                profilePictureUrl
        );
    }

}
