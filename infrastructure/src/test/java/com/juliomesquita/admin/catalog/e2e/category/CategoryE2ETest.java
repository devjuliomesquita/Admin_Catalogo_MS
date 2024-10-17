package com.juliomesquita.admin.catalog.e2e.category;

import com.juliomesquita.admin.catalog.E2ETest;
import com.juliomesquita.admin.catalog.domain.category.CategoryId;
import com.juliomesquita.admin.catalog.infrastructure.api.models.CreateCategoryRequest;
import com.juliomesquita.admin.catalog.infrastructure.category.persistence.CategoryEntity;
import com.juliomesquita.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import com.juliomesquita.admin.catalog.infrastructure.configuration.mapper.json.Json;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@E2ETest
public class CategoryE2ETest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Container
    private static final MySQLContainer<?> MY_SQL_CONTAINER =
            new MySQLContainer<>("mysql:latest")
                    .withPassword("password")
                    .withUsername("root")
                    .withDatabaseName("adm_videos");

    @DynamicPropertySource
    public static void setDataSourceProperties(final DynamicPropertyRegistry registry) {
        registry.add("mysql.port", () -> MY_SQL_CONTAINER.getMappedPort(3306));
    }

    @Test
    void asACatalogAdminIShouldBeAbleToCreateANewCategoryWithValidValues() throws Exception {
        //verify container and Database
        assertTrue(MY_SQL_CONTAINER.isRunning());
        assertEquals(0, this.categoryRepository.count());

        //given
        final String expectedName = "Terror";
        final String expectedDescription = "Categoria mais assistida.";
        final boolean expectedIsActive = true;

        //when
        final CategoryId aCategoryId =
                this.givenCategory(expectedName, expectedDescription, expectedIsActive);

        //verify database
        assertEquals(1, this.categoryRepository.count());

        final CategoryEntity aCategoryRetrieved = this.categoryRepository.findById(aCategoryId.getValue()).get();

        //then
        assertAll("Verify attributes Category", () -> {
            assertEquals(aCategoryId.getValue(), aCategoryRetrieved.getId());
            assertEquals(expectedName, aCategoryRetrieved.getName());
            assertEquals(expectedDescription, aCategoryRetrieved.getDescription());
            assertEquals(expectedIsActive, aCategoryRetrieved.isActive());
            assertNotNull(aCategoryRetrieved.getCreatedAt());
            assertNotNull(aCategoryRetrieved.getUpdatedAt());
            assertNull(aCategoryRetrieved.getDeletedAt());
        });



    }

    private CategoryId givenCategory(
            final String aName,
            final String aDescription,
            final boolean aIsActive
    ) throws Exception {
        final CreateCategoryRequest input =
                new CreateCategoryRequest(aName, aDescription, aIsActive);
        final String anIdCreated = this.given("/categories", input);
        return CategoryId.from(anIdCreated);
    }

    private String given(final String url, final Object body) throws Exception {
        final MockHttpServletRequestBuilder request = post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(Json.writeValueAsString(body));
        return Objects.requireNonNull(this.mockMvc.perform(request)
                        .andExpect(status().isCreated())
                        .andReturn()
                        .getResponse()
                        .getHeader("Location"))
                .replace("%s/".formatted(url), "");
    }
}
