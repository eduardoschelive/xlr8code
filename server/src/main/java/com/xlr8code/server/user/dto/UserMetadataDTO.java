package com.xlr8code.server.user.dto;

import com.xlr8code.server.user.entity.User;
import com.xlr8code.server.user.entity.UserMetadata;

public record UserMetadataDTO(
        String profilePictureUrl
) {
    public static UserMetadataDTO from(UserMetadata userMetadata) {
        return new UserMetadataDTO(
                userMetadata.getProfilePictureUrl()
        );
    }

    public UserMetadata toUserMetadata(User user) {
        return UserMetadata.builder()
                .user(user)
                .profilePictureUrl(this.profilePictureUrl())
                .build();
    }

}
