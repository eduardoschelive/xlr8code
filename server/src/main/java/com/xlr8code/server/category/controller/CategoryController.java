package com.xlr8code.server.category.controller;

import com.xlr8code.server.article.entity.Article;
import com.xlr8code.server.category.dto.CategoryDTO;
import com.xlr8code.server.category.dto.TranslatedCategoryDTO;
import com.xlr8code.server.category.entity.Category;
import com.xlr8code.server.category.service.CategoryService;
import com.xlr8code.server.common.annotation.MultiLanguageContent;
import com.xlr8code.server.common.service.LocaleService;
import com.xlr8code.server.common.utils.Endpoint;
import com.xlr8code.server.filter.annotation.FilterEndpoint;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping(Endpoint.Categories.BASE_PATH)
@RequiredArgsConstructor
@Tag(name = "Category")
public class CategoryController {

    private final CategoryService categoryService;
    private final LocaleService localeService;

    @PostMapping
    public ResponseEntity<Void> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        var category = categoryService.create(categoryDTO);
        return ResponseEntity.created(URI.create(Endpoint.Categories.BASE_PATH + "/" + category.getId())).build();
    }

    @GetMapping
    @MultiLanguageContent
    @FilterEndpoint(Category.class)
    public ResponseEntity<Page<TranslatedCategoryDTO>> listCategories(Specification<Category> specification, Pageable pageable, HttpServletRequest request) {
        var languages = localeService.getAllAcceptedLanguages(request);
        var result = categoryService.findAll(specification, pageable, languages);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    @MultiLanguageContent
    public ResponseEntity<TranslatedCategoryDTO> findCategory(@PathVariable String id, HttpServletRequest request) {
        var languages = localeService.getAllAcceptedLanguages(request);
        var result = categoryService.findById(id, languages);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TranslatedCategoryDTO> updateCategory(@PathVariable String id, @Valid @RequestBody CategoryDTO categoryDTO) {
        var updated = categoryService.update(id, categoryDTO);
        return ResponseEntity.ok(updated);
    }

}
