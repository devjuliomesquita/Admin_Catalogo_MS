package com.juliomesquita.admin.catalog.infrastructure.api.models;

import com.juliomesquita.admin.catalog.JacksonTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JacksonTest
class CreateCategoryRequestTest {
    @Autowired
    private JacksonTester<CreateCategoryRequest> json;

    @Test
    void testMarshall() throws Exception {
        //given
        final String expectedName = "Animes";
        final String expectedDescription = "Shonen";
        final Boolean expectedActive = true;

        final CreateCategoryRequest request = new CreateCategoryRequest(
                expectedName, expectedDescription, expectedActive);

        //when
        final JsonContent<CreateCategoryRequest> jsonContent = this.json.write(request);

        //then
        assertThat(jsonContent)
                .hasJsonPathValue("$.name", expectedName)
                .hasJsonPathValue("$.description", expectedDescription)
                .hasJsonPathValue("$.is_active", expectedActive);
    }

    @Test
    void testUnmarshall() throws Exception {
        //given
        final String expectedName = "Animes";
        final String expectedDescription = "Shonen";
        final Boolean expectedActive = true;

        final String jsonCreated = """
                {
                    "name": "%s",
                    "description": "%s",
                    "is_active": %s
                }
                """.formatted(
                expectedName,
                expectedDescription,
                expectedActive);

        //when
        final ObjectContent<CreateCategoryRequest> objectContent = this.json.parse(jsonCreated);

        //then
        assertThat(objectContent)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("description", expectedDescription)
                .hasFieldOrPropertyWithValue("active", expectedActive);
    }

}