package com.xlr8code.server.user.service;

import com.xlr8code.server.common.utils.UUIDUtils;
import com.xlr8code.server.user.dto.UpdateUserMetadataDTO;
import com.xlr8code.server.user.dto.UserMetadataDTO;
import com.xlr8code.server.user.exception.UserMetadataNotFoundException;
import com.xlr8code.server.user.repository.UserMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserMetadataService {

    private final UserMetadataRepository userMetadataRepository;

    @Transactional
    public UserMetadataDTO updateUserMetadata(String uuidString, UpdateUserMetadataDTO updateUserMetadataDTO) {
        var uuid = UUIDUtils.convertFromString(uuidString)
                .orElseThrow(UserMetadataNotFoundException::new);

        return this.updateUserMetadata(uuid, updateUserMetadataDTO);
    }

    @Transactional
    public UserMetadataDTO updateUserMetadata(UUID uuid, UpdateUserMetadataDTO updateUserMetadataDTO) {
        var userMetadata = userMetadataRepository.findById(uuid)
                .orElseThrow(UserMetadataNotFoundException::new);

        userMetadata.setProfilePictureUrl(updateUserMetadataDTO.profilePictureUrl());
        userMetadata.setLanguagePreference(updateUserMetadataDTO.languagePreference());
        userMetadata.setThemePreference(updateUserMetadataDTO.themePreference());

        var updatedUserMetadata = this.userMetadataRepository.save(userMetadata);

        return UserMetadataDTO.fromUserMetadata(updatedUserMetadata);
    }

}
