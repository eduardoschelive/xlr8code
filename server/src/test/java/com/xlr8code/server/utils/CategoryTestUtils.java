package com.xlr8code.server.utils;

import com.xlr8code.server.common.enums.Language;
import com.xlr8code.server.category.dto.CategoryDTO;
import com.xlr8code.server.category.dto.CategoryLanguageDTO;

import java.util.Map;

public class CategoryTestUtils {

    public static CategoryDTO buildCategoryDTO() {
        Map<Language, CategoryLanguageDTO> languages = Map.of(
                Language.AMERICAN_ENGLISH, new CategoryLanguageDTO("title", "slug1", "description"),
                Language.BRAZILIAN_PORTUGUESE, new CategoryLanguageDTO("titulo", "slug2", "descrição")
        );

        return new CategoryDTO(languages);
    }

}
