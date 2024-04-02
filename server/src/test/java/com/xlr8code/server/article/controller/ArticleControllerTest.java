package com.xlr8code.server.article.controller;

import com.xlr8code.server.article.entity.Article;
import com.xlr8code.server.article.service.ArticleService;
import com.xlr8code.server.common.utils.Endpoint;
import com.xlr8code.server.series.service.SeriesService;
import com.xlr8code.server.user.entity.User;
import com.xlr8code.server.user.utils.UserRole;
import com.xlr8code.server.utils.ArticleTestUtils;
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

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
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

    @Nested
    class CreateTests {

            @Test
            void it_should_return_201_created() throws Exception {
                // given
                var createArticleDTO = ArticleTestUtils.buildArticleDTO();
                var uuidToReturn = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
                // when

                when(articleService.create(createArticleDTO)).thenReturn(Article.builder().id(uuidToReturn).build());

                // then
                mockMvc.perform(post(Endpoint.Article.BASE_PATH)
                                .with(SecurityMockMvcRequestPostProcessors.user(admin))
                                .header("Accept-Language", "en_US, pt_BR")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JsonTestUtils.asJsonString(createArticleDTO)))
                        .andExpect(status().isCreated());
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
        void it_should_return_204_no_content() throws Exception {
            var articleId = UUID.randomUUID().toString();
            var updateArticleDTO = ArticleTestUtils.buildArticleDTO();

            mockMvc.perform(put(Endpoint.Article.BASE_PATH + "/" + articleId)
                            .with(SecurityMockMvcRequestPostProcessors.user(admin))
                            .header("Accept-Language", "en_US, pt_BR")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonTestUtils.asJsonString(updateArticleDTO)))
                    .andExpect(status().isNoContent());
        }

    }

    @Nested
    class GetTests {

        @Test
        void it_should_return_200_ok() throws Exception {
            var articleId = UUID.randomUUID().toString();

            mockMvc.perform(get(Endpoint.Article.BASE_PATH + "/" + articleId))
                    .andExpect(status().isOk());
        }

    }


}