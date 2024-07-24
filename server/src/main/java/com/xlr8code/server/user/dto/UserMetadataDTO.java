package com.xlr8code.server.user.dto;

import com.xlr8code.server.user.entity.User;
import com.xlr8code.server.user.entity.UserMetadata;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "UserMetadata")
public record UserMetadataDTO(
        @Schema(description = "The profile picture URL of the user. Can be null if the user has not set a profile picture")
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
