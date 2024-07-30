package com.xlr8code.server.category.controller;

import com.xlr8code.server.category.dto.CategoryDTO;
import com.xlr8code.server.category.dto.TranslatedCategoryDTO;
import com.xlr8code.server.category.entity.Category;
import com.xlr8code.server.category.exception.CategoryNotFoundException;
import com.xlr8code.server.category.service.CategoryService;
import com.xlr8code.server.common.annotation.MultiLanguageContent;
import com.xlr8code.server.common.exception.DuplicateSlugInLanguagesException;
import com.xlr8code.server.common.exception.SlugAlreadyExistsException;
import com.xlr8code.server.common.service.LocaleService;
import com.xlr8code.server.common.utils.Endpoint;
import com.xlr8code.server.filter.annotation.FilterEndpoint;
import com.xlr8code.server.filter.utils.FilterUtils;
import com.xlr8code.server.openapi.annotation.ErrorResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

@RestController
@RequestMapping(Endpoint.Categories.BASE_PATH)
@RequiredArgsConstructor
@Tag(name = "Category")
public class CategoryController {

    private final CategoryService categoryService;
    private final LocaleService localeService;

    @Operation(
            summary = "Create category",
            description = "Use this endpoint to create a new category.",
            responses = @ApiResponse(responseCode = "201")
    )
    @ErrorResponses(value = {
            DuplicateSlugInLanguagesException.class,
            SlugAlreadyExistsException.class
    })
    @PostMapping
    public ResponseEntity<Void> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        var category = categoryService.create(categoryDTO);
        return ResponseEntity.created(URI.create(Endpoint.Categories.BASE_PATH + "/" + category.getId())).build();
    }

    @Operation(
            summary = "List categories",
            description = "Use this  endpoint to list categories and filter categories by their fields.",
            responses = @ApiResponse(responseCode = "200")
    )
    @GetMapping
    @MultiLanguageContent
    @FilterEndpoint(Category.class)
    public ResponseEntity<Page<TranslatedCategoryDTO>> listCategories(Specification<Category> specification, Pageable pageable, HttpServletRequest request) {
        var languages = localeService.getAllAcceptedLanguages(request);
        var result = categoryService.findAll(specification, pageable, languages);
        return FilterUtils.buildResponseEntity(result);
    }

    @Operation(
            summary = "Find category",
            description = "Use this  endpoint to find one specific category.",
            responses = @ApiResponse(responseCode = "200")
    )
    @ErrorResponses(value = CategoryNotFoundException.class)
    @GetMapping("/{id}")
    @MultiLanguageContent
    public ResponseEntity<TranslatedCategoryDTO> findCategory(
            @Schema(description = "The unique identifier of the category") @PathVariable String id,
            HttpServletRequest request
    ) {
        var languages = localeService.getAllAcceptedLanguages(request);
        var result = categoryService.findById(id, languages);
        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "Delete a category",
            description = "Use this  endpoint to delete a category.",
            responses = @ApiResponse(responseCode = "204")
    )
    @ErrorResponses(value = CategoryNotFoundException.class)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@Schema(description = "The unique identifier of the category") @PathVariable String id) {
        this.categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Update a category",
            description = "Use this  endpoint update an existing category.",
            responses = @ApiResponse(responseCode = "200")
    )
    @ErrorResponses(value = {
            CategoryNotFoundException.class,
            DuplicateSlugInLanguagesException.class,
            SlugAlreadyExistsException.class
    })
    @PutMapping("/{id}")
    public ResponseEntity<TranslatedCategoryDTO> updateCategory(
            @Schema(description = "The unique identifier of the category") @PathVariable String id,
            @Valid @RequestBody CategoryDTO categoryDTO
    ) {
        var updated = categoryService.update(id, categoryDTO);
        return ResponseEntity.ok(updated);
    }

}
