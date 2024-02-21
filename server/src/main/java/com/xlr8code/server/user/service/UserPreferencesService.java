package com.xlr8code.server.user.service;

import com.xlr8code.server.common.utils.UUIDUtils;
import com.xlr8code.server.user.dto.UpdateUserPreferencesDTO;
import com.xlr8code.server.user.dto.UserPreferencesDTO;
import com.xlr8code.server.user.entity.UserPreferences;
import com.xlr8code.server.user.exception.UserNotFoundException;
import com.xlr8code.server.user.repository.UserPreferencesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserPreferencesService {

    private final UserPreferencesRepository userPreferencesRepository;

    /**
     * @param uuidString               UUID of the user
     * @param updateUserPreferencesDTO {@link UpdateUserPreferencesDTO} of the user
     */
    public UserPreferencesDTO updateUserPreferencesByUserUUID(String uuidString, UpdateUserPreferencesDTO updateUserPreferencesDTO) {
        var uuid = UUIDUtils.convertFromString(uuidString).orElseThrow(UserNotFoundException::new);
        return this.updateUserPreferencesByUserUUID(uuid, updateUserPreferencesDTO);
    }

    /**
     * @param uuid                     UUID of the user
     * @param updateUserPreferencesDTO {@link UpdateUserPreferencesDTO} of the user
     */
    public UserPreferencesDTO updateUserPreferencesByUserUUID(UUID uuid, UpdateUserPreferencesDTO updateUserPreferencesDTO) {
        var preferences = this.userPreferencesRepository.findById(uuid).orElseThrow(UserNotFoundException::new);

        this.updatePreferencesFields(preferences, updateUserPreferencesDTO);

        return UserPreferencesDTO.from(userPreferencesRepository.save(preferences));
    }

    /**
     * @param preferences              {@link UserPreferences} to be updated
     * @param updateUserPreferencesDTO {@link UpdateUserPreferencesDTO} to update the preferences
     */
    private void updatePreferencesFields(UserPreferences preferences, UpdateUserPreferencesDTO updateUserPreferencesDTO) {
        preferences.setLanguage(updateUserPreferencesDTO.language());
        preferences.setTheme(updateUserPreferencesDTO.theme());
    }

}
