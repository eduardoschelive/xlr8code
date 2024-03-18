package com.xlr8code.server.series.controller;

import com.xlr8code.server.common.enums.Language;
import com.xlr8code.server.common.utils.Endpoint;
import com.xlr8code.server.series.dto.SeriesDTO;
import com.xlr8code.server.series.dto.SeriesLanguageDTO;
import com.xlr8code.server.series.dto.TranslatedSeriesDTO;
import com.xlr8code.server.series.entity.Series;
import com.xlr8code.server.series.service.SeriesService;
import com.xlr8code.server.user.entity.User;
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

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@AutoConfigureMockMvc
class SeriesControllerTest {

    private final User admin = UserTestUtils.buildUserDetails(UUID.randomUUID(), Set.of(UserRole.ADMIN.toRole()));
    @MockBean
    private SeriesService seriesService;
    @Autowired
    private MockMvc mockMvc;

    @Nested
    class CreateTests {

        @Test
        void it_should_return_201_created() throws Exception {
            // given
            var createSeriesDTO = buildCreateSeriesDTO();
            var uuidToReturn = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
            // when
            when(seriesService.create(createSeriesDTO)).thenReturn(Series.builder().id(uuidToReturn).build());
            // then
            mockMvc.perform(post(Endpoint.Series.BASE_PATH)
                            .with(SecurityMockMvcRequestPostProcessors.user(admin))
                            .header("Accept-Language", "en_US, pt_BR")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonTestUtils.asJsonString(createSeriesDTO)))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", Endpoint.Series.BASE_PATH + "/" + uuidToReturn));
        }

        private SeriesDTO buildCreateSeriesDTO() {
            Map<Language, SeriesLanguageDTO> languages = Map.of(
                    Language.AMERICAN_ENGLISH, new SeriesLanguageDTO("Test Series", "test-series", "Test Series Description"),
                    Language.BRAZILIAN_PORTUGUESE, new SeriesLanguageDTO("Série de Teste", "serie-de-teste", "Descrição da Série de Teste")
            );

            return new SeriesDTO(languages);
        }

    }

    @Nested
    class FindTests {

        @Test
        void it_should_return_page_of_series() throws Exception {
            mockMvc.perform(get(Endpoint.Series.BASE_PATH)
                            .header("Accept-Language", "en_US, pt_BR"))
                    .andExpect(status().isOk());
        }

        @Test
        void it_should_return_series() throws Exception {
            String uuid = "123e4567-e89b-12d3-a456-426614174000";
            when(seriesService.findById(UUID.fromString(uuid))).thenReturn(Series.builder().id(UUID.fromString(uuid)).build());

            mockMvc.perform(get(Endpoint.Series.BASE_PATH + "/uuid")
                            .header("Accept-Language", "en_US, pt_BR"))
                    .andExpect(status().isOk());
        }

        @Test
        void it_should_return_page_of_series_when_searching() throws Exception {
            mockMvc.perform(get(Endpoint.Series.BASE_PATH + "/search?query=test")
                            .header("Accept-Language", "en_US, pt_BR"))
                    .andExpect(status().isOk());
        }

    }

    @Nested
    class DeleteTests {

        @Test
        void it_should_return_204_no_content() throws Exception {
            String uuid = "123e4567-e89b-12d3-a456-426614174000";
            when(seriesService.findById(UUID.fromString(uuid))).thenReturn(Series.builder().id(UUID.fromString(uuid)).build());

            mockMvc.perform(delete(Endpoint.Series.BASE_PATH + "/uuid")
                            .with(SecurityMockMvcRequestPostProcessors.user(admin)))
                    .andExpect(status().isNoContent());
        }

    }

    @Nested
    class UpdateTests {

        @Test
        void it_should_return_200_ok() throws Exception {
            String uuid = "123e4567-e89b-12d3-a456-426614174000";
            var updateSeriesDTO = buildCreateSeriesDTO();
            when(seriesService.update(uuid, updateSeriesDTO)).thenReturn(new TranslatedSeriesDTO(null, null));

            mockMvc.perform(put(Endpoint.Series.BASE_PATH + "/uuid")
                            .with(SecurityMockMvcRequestPostProcessors.user(admin))
                            .header("Accept-Language", "en_US, pt_BR")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonTestUtils.asJsonString(updateSeriesDTO)))
                    .andExpect(status().isOk());
        }

        private SeriesDTO buildCreateSeriesDTO() {
            Map<Language, SeriesLanguageDTO> languages = Map.of(
                    Language.AMERICAN_ENGLISH, new SeriesLanguageDTO("Test Series", "test-series", "Test Series Description"),
                    Language.BRAZILIAN_PORTUGUESE, new SeriesLanguageDTO("Série de Teste", "serie-de-teste", "Descrição da Série de Teste")
            );

            return new SeriesDTO(languages);
        }

    }

}