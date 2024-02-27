package com.xlr8code.server.user.service;

import com.xlr8code.server.user.dto.UpdateUserMetadataDTO;
import com.xlr8code.server.user.entity.User;
import com.xlr8code.server.user.repository.UserRepository;
import com.xlr8code.server.utils.UserTestUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserMetadataServiceTest {

    private static final String DEFAULT_USERNAME = "test";
    private static final String DEFAULT_EMAIL = "test@gmail.com";
    private static final String DEFAULT_PASSWORD = "test";

    private User defaultUser;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMetadataService userMetadataService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        var user = UserTestUtils.buildCreateUserDTO(DEFAULT_USERNAME, DEFAULT_EMAIL, DEFAULT_PASSWORD);
        this.defaultUser = userService.create(user);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        this.defaultUser = null;
    }

    @Test
    void it_should_update_metadata() {
        var update = new UpdateUserMetadataDTO("new_profile_picture_url");
        var uuid = defaultUser.getId().toString();

        var updatedMetadata = userMetadataService.updateMetadataByUserUUID(uuid, update);

        assertEquals(updatedMetadata.profilePictureUrl(), update.profilePictureUrl());
    }

}
