package com.xlr8code.server.user.service;

import com.xlr8code.server.common.utils.Language;
import com.xlr8code.server.common.utils.Theme;
import com.xlr8code.server.user.dto.UpdateUserMetadataDTO;
import com.xlr8code.server.user.entity.User;
import com.xlr8code.server.user.repository.UserRepository;
import com.xlr8code.server.utils.UserTestUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserMetadataServiceTest {

    private static final String DEFAULT_USERNAME = "test";
    private static final String DEFAULT_EMAIL = "test@test.com";
    private static final String DEFAULT_PASSWORD = "test";

    @Autowired
    private UserMetadataService userMetadataService;

    private User defaultUser;

    @BeforeEach
    void setUp(@Autowired UserService userService) {
        var user = UserTestUtils.buildCreateUserDTO(DEFAULT_USERNAME, DEFAULT_EMAIL, DEFAULT_PASSWORD);
        this.defaultUser = userService.create(user);
    }

    @AfterEach
    void tearDown(@Autowired UserRepository userRepository) {
        userRepository.deleteAll();
        this.defaultUser = null;
    }

    @Test
    void it_should_update_user_metadata() {
        var updateUserMetadataDTO = new UpdateUserMetadataDTO(Theme.DARK, Language.AMERICAN_ENGLISH, "https://www.test.com");

        var updatedMetadata = this.userMetadataService.updateUserMetadata(this.defaultUser.getId(), updateUserMetadataDTO);

        assertNotNull(updatedMetadata);
        assertEquals(updateUserMetadataDTO.profilePictureUrl(), updatedMetadata.profilePictureUrl());
        assertEquals(updateUserMetadataDTO.languagePreference(), updatedMetadata.languagePreference());
        assertEquals(updateUserMetadataDTO.themePreference(), updatedMetadata.themePreference());
    }


}