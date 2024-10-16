package com.juliomesquita.admin.catalog.infrastructure.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record ListCategoriesResponse(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("description") String description,
        @JsonProperty("is_active") Boolean active,
        @JsonProperty("created_at") Instant createdAt
) {
}
