package com.xlr8code.server.category.controller;

import com.xlr8code.server.common.annotation.MultiLanguageContent;
import com.xlr8code.server.common.service.LocaleService;
import com.xlr8code.server.common.utils.Endpoint;
import com.xlr8code.server.category.dto.CategoryDTO;
import com.xlr8code.server.category.dto.TranslatedCategoryDTO;
import com.xlr8code.server.category.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(Endpoint.Categories.BASE_PATH)
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final LocaleService localeService;

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody CategoryDTO categoryDTO) {
        var createdSeries = categoryService.create(categoryDTO);
        return ResponseEntity.created(URI.create(Endpoint.Categories.BASE_PATH + "/" + createdSeries.getId())).build();
    }

    @GetMapping
    @MultiLanguageContent
    public ResponseEntity<Page<TranslatedCategoryDTO>> findAll(Pageable pageable, HttpServletRequest request) {
        var languages = localeService.getAllAcceptedLanguages(request);
        var result = categoryService.findAll(languages, pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    @MultiLanguageContent
    public ResponseEntity<TranslatedCategoryDTO> findById(@PathVariable String id, HttpServletRequest request) {
        var languages = localeService.getAllAcceptedLanguages(request);
        var result = categoryService.findById(id, languages);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/search")
    @MultiLanguageContent
    public ResponseEntity<Page<TranslatedCategoryDTO>> search(@RequestParam String query, Pageable pageable, HttpServletRequest request) {
        var languages = localeService.getAllAcceptedLanguages(request);
        var result = categoryService.search(query, languages, pageable);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TranslatedCategoryDTO> update(@PathVariable String id, @Valid @RequestBody CategoryDTO categoryDTO) {
        var updated = categoryService.update(id, categoryDTO);
        return ResponseEntity.ok(updated);
    }

}
