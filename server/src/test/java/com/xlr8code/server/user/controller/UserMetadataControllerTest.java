package com.xlr8code.server.user.controller;

import com.xlr8code.server.common.utils.Endpoint;
import com.xlr8code.server.common.utils.Language;
import com.xlr8code.server.common.utils.Theme;
import com.xlr8code.server.user.dto.UpdateUserMetadataDTO;
import com.xlr8code.server.user.dto.UserMetadataDTO;
import com.xlr8code.server.user.entity.User;
import com.xlr8code.server.user.exception.UserMetadataNotFoundException;
import com.xlr8code.server.user.service.UserMetadataService;
import com.xlr8code.server.user.utils.UserRole;
import com.xlr8code.server.utils.JsonTestUtils;
import com.xlr8code.server.utils.UserTestUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserMetadataControllerTest {

    @MockBean
    private UserMetadataService userMetadataService;

    @Autowired
    private MockMvc mockMvc;

    @Nested
    class UpdateUserMetadataTests {
        private final User member = UserTestUtils.buildUserDetails(UUID.randomUUID(), Set.of(UserRole.MEMBER.toRole()));
        private final User admin = UserTestUtils.buildUserDetails(UUID.randomUUID(), Set.of(UserRole.ADMIN.toRole()));

        @Test
        void it_should_update_user_metadata() throws Exception {
            var updateUserMetadataDTO = new UpdateUserMetadataDTO(Theme.DARK, Language.AMERICAN_ENGLISH, "https://www.test.com");

            when(userMetadataService.updateUserMetadata(member.getId(), updateUserMetadataDTO)).thenReturn(expectedUserMetadataDTO());

            mockMvc.perform(put(Endpoint.UserMetadata.BASE_PATH + "/{uuid}", member.getId())
                            .with(SecurityMockMvcRequestPostProcessors.user(member))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonTestUtils.asJsonString(updateUserMetadataDTO)))
                    .andExpect(status().isOk());
        }

        @Test
        void it_should_not_update_user_metadata_of_other_user_without_admin() throws Exception {
            var updateUserMetadataDTO = new UpdateUserMetadataDTO(Theme.DARK, Language.AMERICAN_ENGLISH, "https://www.test.com");

            mockMvc.perform(put(Endpoint.UserMetadata.BASE_PATH + "/{uuid}", admin.getId())
                            .with(SecurityMockMvcRequestPostProcessors.user(member))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonTestUtils.asJsonString(updateUserMetadataDTO)))
                    .andExpect(status().isForbidden());
        }

        @Test
        void it_should_update_user_metadata_of_other_user_with_admin() throws Exception {
            var updateUserMetadataDTO = new UpdateUserMetadataDTO(Theme.DARK, Language.AMERICAN_ENGLISH, "https://www.test.com");

            when(userMetadataService.updateUserMetadata(admin.getId(), updateUserMetadataDTO)).thenReturn(expectedUserMetadataDTO());

            mockMvc.perform(put(Endpoint.UserMetadata.BASE_PATH + "/{uuid}", admin.getId())
                            .with(SecurityMockMvcRequestPostProcessors.user(admin))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonTestUtils.asJsonString(updateUserMetadataDTO)))
                    .andExpect(status().isOk());
        }

        @Test
        void it_should_not_update_user_metadata_of_non_existing_user() throws Exception {
            var updateUserMetadataDTO = new UpdateUserMetadataDTO(Theme.DARK, Language.AMERICAN_ENGLISH, "https://www.test.com");
            var uuid = UUID.randomUUID().toString();


            when(userMetadataService.updateUserMetadata(uuid, updateUserMetadataDTO)).thenThrow(UserMetadataNotFoundException.class);

            mockMvc.perform(put(Endpoint.UserMetadata.BASE_PATH + "/{uuid}", uuid)
                            .with(SecurityMockMvcRequestPostProcessors.user(admin))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonTestUtils.asJsonString(updateUserMetadataDTO)))
                    .andExpect(status().isNotFound());
        }

    }

    private UserMetadataDTO expectedUserMetadataDTO() {
        return new UserMetadataDTO(Theme.DARK, Language.AMERICAN_ENGLISH,  "https://www.test.com");
    }

}