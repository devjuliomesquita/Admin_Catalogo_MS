package com.juliomesquita.admin.catalog.application.usecases.category.create;

import com.juliomesquita.admin.catalog.domain.category.Category;
import com.juliomesquita.admin.catalog.domain.category.CategoryId;

public record CreateCategoryOutput(
        String id
) {
    public static CreateCategoryOutput from(Category aCategory) {
        return new CreateCategoryOutput(aCategory.getId().getValue());
    }

    public static CreateCategoryOutput from(String anId) {
        return new CreateCategoryOutput(CategoryId.from(anId).getValue());
    }
}
