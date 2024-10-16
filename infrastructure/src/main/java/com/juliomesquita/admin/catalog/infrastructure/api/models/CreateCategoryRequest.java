package com.juliomesquita.admin.catalog.infrastructure.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.juliomesquita.admin.catalog.application.usecases.category.create.CreateCategoryCommand;

public record CreateCategoryRequest(
        @JsonProperty("name") String name,
        @JsonProperty("description") String description,
        @JsonProperty("is_active") Boolean active
) {
    public static CreateCategoryCommand toApp(final CreateCategoryRequest anInput) {
        return new CreateCategoryCommand(
                anInput.name(),
                anInput.description(),
                anInput.active() != null ? anInput.active() : true
        );
    }
}
