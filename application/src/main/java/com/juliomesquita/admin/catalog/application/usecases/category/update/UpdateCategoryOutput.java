package com.juliomesquita.admin.catalog.application.usecases.category.update;

import com.juliomesquita.admin.catalog.domain.category.Category;

public record UpdateCategoryOutput(String id) {
    public static UpdateCategoryOutput from(final Category aCategory){
        return new UpdateCategoryOutput(aCategory.getId().getValue());
    }

    public static UpdateCategoryOutput from(final String anId){
        return new UpdateCategoryOutput(anId);
    }
}
