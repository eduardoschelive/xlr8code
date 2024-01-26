package com.xlr8code.server.user.controller;

import com.xlr8code.server.authentication.utils.Endpoint;
import com.xlr8code.server.common.utils.Language;
import com.xlr8code.server.common.utils.Theme;
import com.xlr8code.server.user.dto.UserDTO;
import com.xlr8code.server.user.dto.UserMetadataDTO;
import com.xlr8code.server.user.entity.User;
import com.xlr8code.server.user.exception.UserNotFoundException;
import com.xlr8code.server.user.service.UserService;
import com.xlr8code.server.user.utils.UserRole;
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

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    private UserDTO expectedUserDTO() {
        return new UserDTO(
                UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
                "test",
                "test@test",
                true,
                LocalDateTime.now(),
                LocalDateTime.now(),
                Set.of("ROLE_USER"),
                new UserMetadataDTO(
                        Theme.LIGHT,
                        Language.BRAZILIAN_PORTUGUESE,
                        null
                )
        );
    }

}