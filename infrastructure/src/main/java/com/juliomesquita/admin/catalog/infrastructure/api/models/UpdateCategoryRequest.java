package com.juliomesquita.admin.catalog.infrastructure.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.juliomesquita.admin.catalog.application.usecases.category.update.UpdateCategoryCommand;

public record UpdateCategoryRequest(
        @JsonProperty("name") String name,
        @JsonProperty("description") String description,
        @JsonProperty("is_active") Boolean active
) {
    public static UpdateCategoryCommand toApp(final String anId, final UpdateCategoryRequest input) {
        return new UpdateCategoryCommand(
                anId,
                input.name(),
                input.description(),
                input.active() != null ? input.active() : true
        );
    }
}
