package com.xlr8code.server.series.controller;

import com.xlr8code.server.common.annotation.MultiLanguageContent;
import com.xlr8code.server.common.service.LocaleService;
import com.xlr8code.server.common.utils.Endpoint;
import com.xlr8code.server.series.dto.CreateSeriesDTO;
import com.xlr8code.server.series.dto.TranslatedSeriesDTO;
import com.xlr8code.server.series.service.SeriesService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(Endpoint.Series.BASE_PATH)
@RequiredArgsConstructor
public class SeriesController {

    private final SeriesService seriesService;
    private final LocaleService localeService;

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody CreateSeriesDTO createSeriesDTO) {
        var createdSeries = seriesService.create(createSeriesDTO);
        return ResponseEntity.created(URI.create(Endpoint.Series.BASE_PATH + "/" + createdSeries.getId())).build();
    }

    @GetMapping
    @MultiLanguageContent
    public ResponseEntity<Page<TranslatedSeriesDTO>> findAll(Pageable pageable, HttpServletRequest request) {
        var languages = localeService.getAllAcceptedLanguages(request);
        var result = seriesService.findAll(languages, pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    @MultiLanguageContent
    public ResponseEntity<TranslatedSeriesDTO> findById(@PathVariable String id, HttpServletRequest request) {
        var languages = localeService.getAllAcceptedLanguages(request);
        var result = seriesService.findById(id, languages);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/search")
    @MultiLanguageContent
    // TODO: change response when no search is passed
    public ResponseEntity<Page<TranslatedSeriesDTO>> search(@RequestParam String query, Pageable pageable, HttpServletRequest request) {
        var languages = localeService.getAllAcceptedLanguages(request);
        var result = seriesService.search(query, languages, pageable);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        seriesService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
