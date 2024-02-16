package com.xlr8code.server.user.controller;

import com.xlr8code.server.common.utils.Endpoint;
import com.xlr8code.server.common.utils.Language;
import com.xlr8code.server.common.utils.Theme;
import com.xlr8code.server.user.dto.UpdateUserMetadataDTO;
import com.xlr8code.server.user.dto.UserDTO;
import com.xlr8code.server.user.dto.UserMetadataDTO;
import com.xlr8code.server.user.entity.User;
import com.xlr8code.server.user.exception.UserMetadataNotFoundException;
import com.xlr8code.server.user.exception.UserNotFoundException;
import com.xlr8code.server.user.service.UserService;
import com.xlr8code.server.user.utils.UserRole;
import com.xlr8code.server.utils.JsonTestUtils;
import com.xlr8code.server.utils.UserTestUtils;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    private UserDTO expectedUserDTO() {
        return new UserDTO(
                UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
                "test",
                "test@test",
                true,
                Instant.now(),
                Instant.now(),
                Set.of("ROLE_USER"),
                new UserMetadataDTO(
                        Theme.LIGHT,
                        Language.BRAZILIAN_PORTUGUESE,
                        null
                )
        );
    }

    @Nested
    class FindTests {

        @Test
        void should_find_by_uuid() throws Exception {
            var expectedUserDTO = expectedUserDTO();

            var uuid = expectedUserDTO.id().toString();

            when(userService.findByUUID(uuid)).thenReturn(expectedUserDTO());

            mockMvc.perform(get(Endpoint.User.BASE_PATH + "/{uuid}", uuid)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath(
                                    "$.id"
                            ).value(expectedUserDTO.id().toString())
                    );

        }

        @Test
        void should_not_find_by_uuid() throws Exception {
            var uuid = UUID.randomUUID().toString();

            when(userService.findByUUID(uuid)).thenThrow(UserNotFoundException.class);

            mockMvc.perform(get(Endpoint.User.BASE_PATH + "/{uuid}", uuid)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());

        }

    }

    @Nested
    class DeleteTests {

        private final User member = UserTestUtils.buildUserDetails(UUID.randomUUID(), Set.of(UserRole.MEMBER.toRole()));
        private final User admin = UserTestUtils.buildUserDetails(UUID.randomUUID(), Set.of(UserRole.ADMIN.toRole()));

        @Test
        void it_should_not_allow_unauthenticated_user_to_delete() throws Exception {
            var uuid = UUID.randomUUID().toString();

            mockMvc.perform(delete(Endpoint.User.BASE_PATH + "/{uuid}", uuid)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden());

        }

        @Test
        void it_should_allow_authenticated_user_to_delete_himself() throws Exception {
            var uuid = member.getId();

            mockMvc.perform(delete(Endpoint.User.BASE_PATH + "/{uuid}", uuid)
                            .with(SecurityMockMvcRequestPostProcessors.user(member))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());

        }

        @Test
        void it_should_not_allow_authenticated_user_to_delete_another_user() throws Exception {
            var uuid = admin.getId();

            mockMvc.perform(delete(Endpoint.User.BASE_PATH + "/{uuid}", uuid)
                            .with(SecurityMockMvcRequestPostProcessors.user(member))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden());

        }

        @Test
        void it_should_allow_admin_to_delete_another_user() throws Exception {
            var uuid = member.getId();

            mockMvc.perform(delete(Endpoint.User.BASE_PATH + "/{uuid}", uuid)
                            .with(SecurityMockMvcRequestPostProcessors.user(admin))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());

        }

    }

    @Nested
    class UpdateTests {

        private final User member = UserTestUtils.buildUserDetails(UUID.randomUUID(), Set.of(UserRole.MEMBER.toRole()));
        private final User admin = UserTestUtils.buildUserDetails(UUID.randomUUID(), Set.of(UserRole.ADMIN.toRole()));

        @Test
        void it_should_update_user() throws Exception {
            var expectedUserDTO = expectedUserDTO();
            var update = UserTestUtils.buildUpdateUserDTO(
                    "new_username",
                    "new_email",
                    "new_password",
                    "new_password"
            );

            when(userService.updateByUUID(expectedUserDTO.id().toString(), update)).thenReturn(expectedUserDTO);

            mockMvc.perform(put(Endpoint.User.BASE_PATH + "/{uuid}", member.getId())
                            .with(SecurityMockMvcRequestPostProcessors.user(member))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonTestUtils.asJsonString(update)))
                    .andExpect(status().isOk());
        }

        @Test
        void it_should_return_error_when_user_cannot_modify_resource() throws Exception {
            var update = UserTestUtils.buildUpdateUserDTO(
                    "new_username",
                    "new_email",
                    "new_password",
                    "new_password"
            );

            var uuid = UUID.randomUUID().toString();

            when(userService.updateByUUID(uuid, update)).thenThrow(UserNotFoundException.class);

            mockMvc.perform(put(Endpoint.User.BASE_PATH + "/{uuid}", uuid)
                            .with(SecurityMockMvcRequestPostProcessors.user(member))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonTestUtils.asJsonString(update)))
                    .andExpect(status().isForbidden());

        }

        @Test
        void it_should_allow_admin_to_update_another_user() throws Exception {
            var expectedUserDTO = expectedUserDTO();
            var update = UserTestUtils.buildUpdateUserDTO(
                    "new_username",
                    "new_email",
                    "new_password",
                    "new_password"
            );

            when(userService.updateByUUID(expectedUserDTO.id().toString(), update)).thenReturn(expectedUserDTO);

            mockMvc.perform(put(Endpoint.User.BASE_PATH + "/{uuid}", member.getId())
                            .with(SecurityMockMvcRequestPostProcessors.user(admin))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonTestUtils.asJsonString(update)))
                    .andExpect(status().isOk());
        }

        @Test
        void it_should_update_user_metadata() throws Exception {
            var updateUserMetadataDTO = new UpdateUserMetadataDTO(Theme.DARK, Language.AMERICAN_ENGLISH, "https://www.test.com");

            when(userService.updateMetadataByUUID(member.getId(), updateUserMetadataDTO)).thenReturn(expectedUserMetadataDTO());

            mockMvc.perform(put(Endpoint.User.BASE_PATH + "/{uuid}" + Endpoint.User.METADATA, member.getId())
                            .with(SecurityMockMvcRequestPostProcessors.user(member))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonTestUtils.asJsonString(updateUserMetadataDTO)))
                    .andExpect(status().isOk());
        }

        @Test
        void it_should_not_update_user_metadata_of_other_user_without_admin() throws Exception {
            var updateUserMetadataDTO = new UpdateUserMetadataDTO(Theme.DARK, Language.AMERICAN_ENGLISH, "https://www.test.com");

            when(userService.updateMetadataByUUID(admin.getId(), updateUserMetadataDTO)).thenReturn(expectedUserMetadataDTO());

            mockMvc.perform(put(Endpoint.User.BASE_PATH + "/{uuid}" + Endpoint.User.METADATA, admin.getId())
                            .with(SecurityMockMvcRequestPostProcessors.user(member))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonTestUtils.asJsonString(updateUserMetadataDTO)))
                    .andExpect(status().isForbidden());
        }

        @Test
        void it_should_update_user_metadata_of_other_user_with_admin() throws Exception {
            var updateUserMetadataDTO = new UpdateUserMetadataDTO(Theme.DARK, Language.AMERICAN_ENGLISH, "https://www.test.com");

            when(userService.updateMetadataByUUID(admin.getId(), updateUserMetadataDTO)).thenReturn(expectedUserMetadataDTO());

            mockMvc.perform(put(Endpoint.User.BASE_PATH + "/{uuid}" + Endpoint.User.METADATA, admin.getId())
                            .with(SecurityMockMvcRequestPostProcessors.user(admin))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonTestUtils.asJsonString(updateUserMetadataDTO)))
                    .andExpect(status().isOk());
        }

        @Test
        void it_should_not_update_user_metadata_of_non_existing_user() throws Exception {
            var updateUserMetadataDTO = new UpdateUserMetadataDTO(Theme.DARK, Language.AMERICAN_ENGLISH, "https://www.test.com");
            var uuid = UUID.randomUUID().toString();


            when(userService.updateMetadataByUUID(uuid, updateUserMetadataDTO)).thenThrow(UserMetadataNotFoundException.class);

            mockMvc.perform(put(Endpoint.User.BASE_PATH + "/{uuid}" + Endpoint.User.METADATA, uuid)
                            .with(SecurityMockMvcRequestPostProcessors.user(admin))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonTestUtils.asJsonString(updateUserMetadataDTO)))
                    .andExpect(status().isNotFound());
        }

        private UserMetadataDTO expectedUserMetadataDTO() {
            return new UserMetadataDTO(Theme.DARK, Language.AMERICAN_ENGLISH, "https://www.test.com");
        }

    }

}