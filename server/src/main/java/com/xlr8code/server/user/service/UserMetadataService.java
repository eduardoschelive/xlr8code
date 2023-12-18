package com.xlr8code.server.user.service;

import com.xlr8code.server.user.entity.User;
import com.xlr8code.server.user.entity.UserMetadata;
import com.xlr8code.server.user.repository.UserMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMetadataService {

    private final UserMetadataRepository userMetadataRepository;

    public UserMetadata create(UserMetadata userMetadata) {
        return this.userMetadataRepository.save(userMetadata);
    }

    public UserMetadata createForUser(User user, UserMetadata userMetadata) {
        userMetadata.setUserId(user.getId());
        return this.create(userMetadata);
    }

}
