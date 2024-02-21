package com.xlr8code.server.user.service;

import com.xlr8code.server.common.utils.UUIDUtils;
import com.xlr8code.server.user.dto.UpdateUserMetadataDTO;
import com.xlr8code.server.user.dto.UserDTO;
import com.xlr8code.server.user.dto.UserMetadataDTO;
import com.xlr8code.server.user.entity.UserMetadata;
import com.xlr8code.server.user.exception.UserNotFoundException;
import com.xlr8code.server.user.repository.UserMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserMetadataService {

    private final UserMetadataRepository userMetadataRepository;

    /**
     * @param uuidString            UUID of the user
     * @param updateUserMetadataDTO {@link UpdateUserMetadataDTO} of the user
     * @return {@link UserDTO} of the updated user
     * @throws UserNotFoundException if the user is not found
     */
    @Transactional
    public UserMetadataDTO updateMetadataByUserUUID(String uuidString, UpdateUserMetadataDTO updateUserMetadataDTO) {
        var uuid = UUIDUtils.convertFromString(uuidString).orElseThrow(UserNotFoundException::new);
        return this.updateMetadataByUserUUID(uuid, updateUserMetadataDTO);
    }

    /**
     * @param uuid                  UUID of the user
     * @param updateUserMetadataDTO {@link UpdateUserMetadataDTO} of the user
     * @return {@link UserDTO} of the updated user
     * @throws UserNotFoundException if the user is not found
     */
    @Transactional
    public UserMetadataDTO updateMetadataByUserUUID(UUID uuid, UpdateUserMetadataDTO updateUserMetadataDTO) {
        var metadata = this.userMetadataRepository.findById(uuid).orElseThrow(UserNotFoundException::new);

        this.updateMetadataFields(metadata, updateUserMetadataDTO);

        var updatedMetadata = userMetadataRepository.save(metadata);

        return UserMetadataDTO.from(updatedMetadata);
    }

    /**
     * @param updateUserMetadataDTO {@link UpdateUserMetadataDTO} to update the metadata
     * @param metadata              {@link UserMetadata} to be updated
     */
    private void updateMetadataFields(UserMetadata metadata, UpdateUserMetadataDTO updateUserMetadataDTO) {
        metadata.setProfilePictureUrl(updateUserMetadataDTO.profilePictureUrl());
    }

}
