package com.juliomesquita.admin.catalog.application.usecases.category.retrive.list;

import com.juliomesquita.admin.catalog.domain.category.Category;

import java.time.Instant;

public record ListCategoriesOutput(
        String id,
        String name,
        String description,
        boolean active,
        Instant createdAt
) {
    public static ListCategoriesOutput from(final Category aCategory){
        return new ListCategoriesOutput(
                aCategory.getId().getValue(),
                aCategory.getName(),
                aCategory.getDescription(),
                aCategory.isActive(),
                aCategory.getCreatedAt()
        );
    }
}
