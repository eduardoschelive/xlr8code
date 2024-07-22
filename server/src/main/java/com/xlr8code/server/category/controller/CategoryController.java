package com.xlr8code.server.category.controller;

import com.xlr8code.server.category.dto.CategoryDTO;
import com.xlr8code.server.category.dto.TranslatedCategoryDTO;
import com.xlr8code.server.category.service.CategoryService;
import com.xlr8code.server.common.annotation.MultiLanguageContent;
import com.xlr8code.server.common.service.LocaleService;
import com.xlr8code.server.common.utils.Endpoint;
import com.xlr8code.server.openapi.annotation.SecuredEndpoint;
import com.xlr8code.server.user.utils.UserRole;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    @SecuredEndpoint(UserRole.ADMIN)
    public ResponseEntity<Void> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        var category = categoryService.create(categoryDTO);
        return ResponseEntity.created(URI.create(Endpoint.Categories.BASE_PATH + "/" + category.getId())).build();
    }

    @GetMapping
    @MultiLanguageContent
    public ResponseEntity<Page<TranslatedCategoryDTO>> listCategories(@RequestParam Map<String, String> requestParams, HttpServletRequest request) {
        var languages = localeService.getAllAcceptedLanguages(request);
        var result = categoryService.findAll(requestParams, languages);
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
    @SecuredEndpoint(UserRole.ADMIN)
    public ResponseEntity<Void> deleteCategory(@PathVariable String id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @SecuredEndpoint(UserRole.ADMIN)
    public ResponseEntity<TranslatedCategoryDTO> updateCategory(@PathVariable String id, @Valid @RequestBody CategoryDTO categoryDTO) {
        var updated = categoryService.update(id, categoryDTO);
        return ResponseEntity.ok(updated);
    }

}
