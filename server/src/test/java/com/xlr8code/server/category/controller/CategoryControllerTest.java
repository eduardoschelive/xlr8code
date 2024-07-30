package com.xlr8code.server.category.controller;

import com.xlr8code.server.category.dto.TranslatedCategoryDTO;
import com.xlr8code.server.category.entity.Category;
import com.xlr8code.server.category.service.CategoryService;
import com.xlr8code.server.common.utils.Endpoint;
import com.xlr8code.server.user.entity.User;
import com.xlr8code.server.user.utils.UserRole;
import com.xlr8code.server.utils.CategoryTestUtils;
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
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@AutoConfigureMockMvc
class CategoryControllerTest {

    private final User admin = UserTestUtils.buildUserDetails(UUID.randomUUID(), Set.of(UserRole.ADMIN.toRole()));
    @MockBean
    private CategoryService categoryService;
    @Autowired
    private MockMvc mockMvc;

    @Nested
    class CreateTests {

        @Test
        void it_should_return_201_created() throws Exception {
            // given
            var categoryDTO = CategoryTestUtils.buildCategoryDTO();
            var uuidToReturn = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
            // when
            when(categoryService.create(categoryDTO)).thenReturn(Category.builder().id(uuidToReturn).build());
            // then
            mockMvc.perform(post(Endpoint.Categories.BASE_PATH)
                            .with(SecurityMockMvcRequestPostProcessors.user(admin))
                            .header("Accept-Language", "en-US, pt-BR")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonTestUtils.asJsonString(categoryDTO)))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", Endpoint.Categories.BASE_PATH + "/" + uuidToReturn));
        }

    }

    @Nested
    class FindTests {

        @Test
        void it_should_return_page_of_category() throws Exception {
            when(categoryService.findAll(
                    any(),
                    any(),
                    any()
            )).thenReturn(Page.empty());

            mockMvc.perform(get(Endpoint.Categories.BASE_PATH)
                            .header("Accept-Language", "en-US, pt-BR"))
                    .andExpect(status().isOk());
        }

        @Test
        void it_should_return_category() throws Exception {
            String uuid = "123e4567-e89b-12d3-a456-426614174000";
            when(categoryService.findById(UUID.fromString(uuid))).thenReturn(Category.builder().id(UUID.fromString(uuid)).build());

            mockMvc.perform(get(Endpoint.Categories.BASE_PATH + "/uuid")
                            .header("Accept-Language", "en-US, pt-BR"))
                    .andExpect(status().isOk());
        }

    }

    @Nested
    class DeleteTests {

        @Test
        void it_should_return_204_no_content() throws Exception {
            String uuid = "123e4567-e89b-12d3-a456-426614174000";
            when(categoryService.findById(UUID.fromString(uuid))).thenReturn(Category.builder().id(UUID.fromString(uuid)).build());

            mockMvc.perform(delete(Endpoint.Categories.BASE_PATH + "/uuid")
                            .with(SecurityMockMvcRequestPostProcessors.user(admin)))
                    .andExpect(status().isNoContent());
        }

    }

    @Nested
    class UpdateTests {

        @Test
        void it_should_return_200_ok() throws Exception {
            String uuid = "123e4567-e89b-12d3-a456-426614174000";
            var categoryDTO = CategoryTestUtils.buildCategoryDTO();
            when(categoryService.update(uuid, categoryDTO)).thenReturn(new TranslatedCategoryDTO(null, null, null, null));

            mockMvc.perform(put(Endpoint.Categories.BASE_PATH + "/uuid")
                            .with(SecurityMockMvcRequestPostProcessors.user(admin))
                            .header("Accept-Language", "en-US, pt-BR")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonTestUtils.asJsonString(categoryDTO)))
                    .andExpect(status().isOk());
        }

    }

}