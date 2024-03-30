package com.xlr8code.server.series.validator;

import com.xlr8code.server.series.service.SeriesService;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ExistingSeriesValidatorTest {

    @MockBean
    private SeriesService seriesService;

    @Test
    void it_should_return_true_when_series_exists() {
        var existingSeriesValidator = new ExistingSeriesValidator(seriesService);

        var seriesId = "seriesId";

        when(seriesService.existsById(seriesId)).thenReturn(true);

        assertTrue(existingSeriesValidator.isValid(seriesId, null));
    }

    @Test
    void it_should_return_false_when_series_does_not_exists() {
        var existingSeriesValidator = new ExistingSeriesValidator(seriesService);
        var seriesId = "";

        when(seriesService.existsById(seriesId)).thenReturn(false);

        assertFalse(existingSeriesValidator.isValid(seriesId, null));
    }

    @Test
    void it_should_return_true_when_series_is_optional_and_blank() {
        var existingSeriesValidator = new ExistingSeriesValidator(seriesService);
        var seriesId = "";
        existingSeriesValidator.optional = true;

        assertTrue(existingSeriesValidator.isValid(seriesId, null));
    }

    @Test
    void it_should_return_false_when_series_is_optional_and_not_blank() {
        var existingSeriesValidator = new ExistingSeriesValidator(seriesService);
        var seriesId = "seriesId";
        existingSeriesValidator.optional = true;

        when(seriesService.existsById(seriesId)).thenReturn(true);

        assertTrue(existingSeriesValidator.isValid(seriesId, null));
    }

}