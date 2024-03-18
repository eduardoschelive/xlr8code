package com.xlr8code.server.series.dto;

import com.xlr8code.server.common.enums.Language;
import com.xlr8code.server.series.entity.Series;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.Map;
import java.util.stream.Collectors;

public record CreateSeriesDTO(
        @NotEmpty
        Map<Language, @Valid CreateSeriesLanguageDTO> languages
) {

        public Series toEntity() {
                var series = new Series();

               var seriesLanguages =  this.languages().entrySet().stream()
                        .map(entry -> entry.getValue().toEntity(series, entry.getKey()))
                        .collect(Collectors.toSet());

                series.setInternationalization(seriesLanguages);


                return series;
        }

}
