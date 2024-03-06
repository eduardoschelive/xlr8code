package com.xlr8code.server.series.controller;

import com.xlr8code.server.common.utils.Endpoint;
import com.xlr8code.server.series.dto.CreateSeriesDTO;
import com.xlr8code.server.series.service.SeriesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping(Endpoint.Series.BASE_PATH)
@RequiredArgsConstructor
public class SeriesController {

    private final SeriesService seriesService;

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody CreateSeriesDTO createSeriesDTO) {
        var createdSeries = seriesService.create(createSeriesDTO);
        return ResponseEntity.created(URI.create(Endpoint.Series.BASE_PATH + "/" + createdSeries.getId())).build();
    }

}
