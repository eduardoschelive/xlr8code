package com.xlr8code.server.article.controller;

import com.xlr8code.server.article.entity.Article;
import com.xlr8code.server.article.service.ArticleService;
import com.xlr8code.server.category.repository.CategoryRepository;
import com.xlr8code.server.category.service.CategoryService;
import com.xlr8code.server.common.utils.Endpoint;
import com.xlr8code.server.user.entity.User;
import com.xlr8code.server.user.utils.UserRole;
import com.xlr8code.server.utils.ArticleTestUtils;
import com.xlr8code.server.utils.CategoryTestUtils;
import com.xlr8code.server.utils.JsonTestUtils;
import com.xlr8code.server.utils.UserTestUtils;
import org.junit.jupiter.api.*;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@AutoConfigureMockMvc
class ArticleControllerTest {

    private final User admin = UserTestUtils.buildUserDetails(UUID.randomUUID(), Set.of(UserRole.ADMIN.toRole()));
    @MockBean
    private ArticleService articleService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryService categoryService;

    @Nested
    class CreateTests {


        @Test
        void it_should_return_201_created() throws Exception {

            var uuidToReturn = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
            var category = categoryService.create(CategoryTestUtils.buildCategoryDTO());

            var createArticleDTO = ArticleTestUtils.buildArticleDTO(category.getId().toString());

            when(articleService.create(createArticleDTO)).thenReturn(Article.builder().id(uuidToReturn).build());

            mockMvc.perform(post(Endpoint.Article.BASE_PATH)
                            .with(SecurityMockMvcRequestPostProcessors.user(admin))
                            .header("Accept-Language", "en-US, pt-BR")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonTestUtils.asJsonString(createArticleDTO)))
                    .andExpect(status().isCreated());

            categoryService.delete(category.getId().toString());
        }

        @Test
        void it_should_not_create_article_when_category_does_not_exist() throws Exception {
            var createArticleDTO = ArticleTestUtils.buildArticleDTO(UUID.randomUUID().toString());

            mockMvc.perform(post(Endpoint.Article.BASE_PATH)
                            .with(SecurityMockMvcRequestPostProcessors.user(admin))
                            .header("Accept-Language", "en-US, pt-BR")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonTestUtils.asJsonString(createArticleDTO)))
                    .andExpect(status().isBadRequest());
        }

    }

    @Nested
    class DeleteTests {

        @Test
        void it_should_return_204_no_content() throws Exception {
            var articleId = UUID.randomUUID().toString();

            mockMvc.perform(delete(Endpoint.Article.BASE_PATH + "/" + articleId)
                            .with(SecurityMockMvcRequestPostProcessors.user(admin)))
                    .andExpect(status().isNoContent());
        }

    }

    @Nested
    class UpdateTests {

        @Test
        void it_should_return_200_when_updating_an_article() throws Exception {
            var articleId = UUID.randomUUID().toString();
            var category = categoryService.create(CategoryTestUtils.buildCategoryDTO());
            var updateArticleDTO = ArticleTestUtils.buildArticleDTO(category.getId().toString());

            mockMvc.perform(put(Endpoint.Article.BASE_PATH + "/" + articleId)
                            .with(SecurityMockMvcRequestPostProcessors.user(admin))
                            .header("Accept-Language", "en-US, pt-BR")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonTestUtils.asJsonString(updateArticleDTO)))
                    .andExpect(status().isOk());

            categoryService.delete(category.getId().toString());
        }

        @Test
        void it_should_not_update_article_when_category_does_not_exist() throws Exception {
            var articleId = UUID.randomUUID().toString();
            var updateArticleDTO = ArticleTestUtils.buildArticleDTO(UUID.randomUUID().toString());

            mockMvc.perform(put(Endpoint.Article.BASE_PATH + "/" + articleId)
                            .with(SecurityMockMvcRequestPostProcessors.user(admin))
                            .header("Accept-Language", "en-US, pt-BR")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonTestUtils.asJsonString(updateArticleDTO)))
                    .andExpect(status().isBadRequest());
        }

    }

    @Nested
    class FindTests {

        @Test
        void it_should_return_200_ok() throws Exception {
            var articleId = UUID.randomUUID().toString();

            mockMvc.perform(get(Endpoint.Article.BASE_PATH + "/" + articleId))
                    .andExpect(status().isOk());
        }

        @Test
        void it_should_return_200_ok_when_finding_all() throws Exception {
            when(articleService.findAll(
                    any(),
                    any(),
                    any()
            )).thenReturn(Page.empty());
            mockMvc.perform(get(Endpoint.Article.BASE_PATH))
                    .andExpect(status().isOk());
        }

    }

}