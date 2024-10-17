package com.juliomesquita.admin.catalog.infrastructure.api.models;

import com.juliomesquita.admin.catalog.JacksonTest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@JacksonTest
class CategoryResponseTest {

    @Autowired
    private JacksonTester<CategoryResponse> json;

    @Test
    void testMarshall() throws Exception {
        //given
        final String expectedId = UUID.randomUUID().toString();
        final String expectedName = "Animes";
        final String expectedDescription = "Shonen";
        final Boolean expectedActive = true;
        final Instant expectedCreatedAt = Instant.now();
        final Instant expectedUpdatedAt = Instant.now();
        final Instant expectedDeletedAt = Instant.now();

        final CategoryResponse response = new CategoryResponse(
                expectedId,
                expectedName,
                expectedDescription,
                expectedActive,
                expectedCreatedAt,
                expectedUpdatedAt,
                expectedDeletedAt
        );

        //when
        final JsonContent<CategoryResponse> jsonContent = this.json.write(response);

        //then
        Assertions.assertThat(jsonContent)
                .hasJsonPathValue("$.id", expectedId)
                .hasJsonPathValue("$.name", expectedName)
                .hasJsonPathValue("$.description", expectedDescription)
                .hasJsonPathValue("$.is_active", expectedActive)
                .hasJsonPathValue("$.created_at", expectedCreatedAt)
                .hasJsonPathValue("$.updated_at", expectedUpdatedAt)
                .hasJsonPathValue("$.deleted_at", expectedDeletedAt);
    }

    @Test
    void testUnmarshall() throws Exception {
        //given
        final String expectedId = "7658";
        final String expectedName = "Animes";
        final String expectedDescription = "Shonen";
        final Boolean expectedActive = true;
        final Instant expectedCreatedAt = Instant.now();
        final Instant expectedUpdatedAt = Instant.now();
        final Instant expectedDeletedAt = Instant.now();

        final String jsonCreated = """
                {
                    "id": "%s",
                    "name": "%s",
                    "description": "%s",
                    "is_active": %s,
                    "created_at": "%s",
                    "updated_at": "%s",
                    "deleted_at": "%s"
                }
                """.formatted(
                expectedId,
                expectedName,
                expectedDescription,
                expectedActive,
                expectedCreatedAt.toString(),
                expectedUpdatedAt.toString(),
                expectedDeletedAt.toString()
        );

        //when
        final ObjectContent<CategoryResponse> objectContent = this.json.parse(jsonCreated);

        //then
        Assertions.assertThat(objectContent)
                .hasFieldOrPropertyWithValue("id", expectedId)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("description", expectedDescription)
                .hasFieldOrPropertyWithValue("active", expectedActive)
                .hasFieldOrPropertyWithValue("createdAt", expectedCreatedAt)
                .hasFieldOrPropertyWithValue("updatedAt", expectedUpdatedAt)
                .hasFieldOrPropertyWithValue("deletedAt", expectedDeletedAt);
    }

}