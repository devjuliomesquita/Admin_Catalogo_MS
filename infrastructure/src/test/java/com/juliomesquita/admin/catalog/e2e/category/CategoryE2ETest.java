package com.juliomesquita.admin.catalog.e2e.category;

import com.juliomesquita.admin.catalog.E2ETest;
import com.juliomesquita.admin.catalog.domain.category.CategoryId;
import com.juliomesquita.admin.catalog.infrastructure.api.models.UpdateCategoryRequest;
import com.juliomesquita.admin.catalog.infrastructure.category.persistence.CategoryEntity;
import com.juliomesquita.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@E2ETest
public class CategoryE2ETest implements MockDslE2E {
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

    @Override
    public MockMvc mockMvc() {
        return this.mockMvc;
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

    @Test
    void asACatalogAdminShouldBeAbleToNavigateToAllCategories() throws Exception {
        //verify container and Database
        assertTrue(MY_SQL_CONTAINER.isRunning());
        assertEquals(0, this.categoryRepository.count());

        //given
        this.givenCategory("Filmes", null, true);
        this.givenCategory("Series", null, true);
        this.givenCategory("Animes", null, true);

        //verify database
        assertEquals(3, this.categoryRepository.count());

        //when
        final ResultActions resultFirstPage = this.listCategories(0, 1);
        final ResultActions resultSecondPage = this.listCategories(1, 1);
        final ResultActions resultThirdPage = this.listCategories(2, 1);
        final ResultActions resultFourthPage = this.listCategories(3, 1);

        //then
        resultFirstPage
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.items_per_page", equalTo(1)))
                .andExpect(jsonPath("$.total_items", equalTo(3)))
                .andExpect(jsonPath("$.total_pages", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Animes")));

        resultSecondPage
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(1)))
                .andExpect(jsonPath("$.items_per_page", equalTo(1)))
                .andExpect(jsonPath("$.total_items", equalTo(3)))
                .andExpect(jsonPath("$.total_pages", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Filmes")));

        resultThirdPage
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(2)))
                .andExpect(jsonPath("$.items_per_page", equalTo(1)))
                .andExpect(jsonPath("$.total_items", equalTo(3)))
                .andExpect(jsonPath("$.total_pages", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Series")));

        resultFourthPage
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(3)))
                .andExpect(jsonPath("$.items_per_page", equalTo(1)))
                .andExpect(jsonPath("$.total_items", equalTo(3)))
                .andExpect(jsonPath("$.total_pages", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(0)));

    }

    @Test
    void asACatalogAdminShouldBeAbleToSearchBetweenCategories() throws Exception {
        //verify container and Database
        assertTrue(MY_SQL_CONTAINER.isRunning());
        assertEquals(0, this.categoryRepository.count());

        //given
        this.givenCategory("Filmes", null, true);
        this.givenCategory("Series", null, true);
        this.givenCategory("Animes", null, true);

        //verify database
        assertEquals(3, this.categoryRepository.count());

        //when
        final ResultActions resultFirstPage = this.listCategories("nim", 0, 1);

        //then
        resultFirstPage
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.items_per_page", equalTo(1)))
                .andExpect(jsonPath("$.total_items", equalTo(1)))
                .andExpect(jsonPath("$.total_pages", equalTo(1)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Animes")));
    }

    @Test
    void asACatalogAdminShouldBeAbleToSortCategories() throws Exception {
        //verify container and Database
        assertTrue(MY_SQL_CONTAINER.isRunning());
        assertEquals(0, this.categoryRepository.count());

        //given
        this.givenCategory("Filmes", "De arrepiar a espinha", true);
        this.givenCategory("Series", "Family friendly", true);
        this.givenCategory("Animes", "Shonen", true);

        //verify database
        assertEquals(3, this.categoryRepository.count());

        //when
        final ResultActions resultFirstPage = this.listCategories("", 0, 3, "description", "desc");

        //then
        resultFirstPage
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.items_per_page", equalTo(3)))
                .andExpect(jsonPath("$.total_items", equalTo(3)))
                .andExpect(jsonPath("$.total_pages", equalTo(1)))
                .andExpect(jsonPath("$.items", hasSize(3)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Animes")))
                .andExpect(jsonPath("$.items[1].name", equalTo("Series")))
                .andExpect(jsonPath("$.items[2].name", equalTo("Filmes")));
    }

    @Test
    void asACatalogAdminShouldBeAbleToBetCategoryByIdentify() throws Exception {
        //verify container and Database
        assertTrue(MY_SQL_CONTAINER.isRunning());
        assertEquals(0, this.categoryRepository.count());

        //given
        final CategoryId categoryId = this.givenCategory("Filmes", "De arrepiar a espinha", true);

        //verify database
        assertEquals(1, this.categoryRepository.count());

        //when
        final ResultActions response = this.retrievedACategory(categoryId.getValue());

        //then
        response
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(categoryId.getValue())))
                .andExpect(jsonPath("$.name", equalTo("Filmes")))
                .andExpect(jsonPath("$.description", equalTo("De arrepiar a espinha")))
                .andExpect(jsonPath("$.is_active", equalTo(true)))
                .andExpect(jsonPath("$.created_at", notNullValue()))
                .andExpect(jsonPath("$.updated_at", notNullValue()))
                .andExpect(jsonPath("$.deleted_at", nullValue()));
    }

    @Test
    void asACatalogAdminIShouldBeAbleToSeeATreatedErrorByGettingANotFoundCategory() throws Exception {
        //verify container and Database
        assertTrue(MY_SQL_CONTAINER.isRunning());
        assertEquals(0, this.categoryRepository.count());

        //when-Throws
        final ResultActions response = this.retrievedACategory("9ae8e82d-330b-403b-b4c8-299e1a526c46");

        //then
        response
                .andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath(
                        "$.message",
                        equalTo("Category with ID 9ae8e82d-330b-403b-b4c8-299e1a526c46 was not found")));
    }

    @Test
    void asACatalogAdminIShouldBeAbleToUpdateACategoryByItsIdentifier() throws Exception {
        //verify container and Database
        assertTrue(MY_SQL_CONTAINER.isRunning());
        assertEquals(0, this.categoryRepository.count());

        //given
        final CategoryId categoryId = this.givenCategory("Filmes", null, true);
        final UpdateCategoryRequest request =
                new UpdateCategoryRequest("Filmes", "De arrepiar a espinha", true);

        //verify database
        assertEquals(1, this.categoryRepository.count());

        //when
        final ResultActions response = this.updateCategory(categoryId.getValue(), request);

        //then
        response
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(categoryId.getValue())));

        final CategoryEntity aCategoryRetrieved = this.categoryRepository.findById(categoryId.getValue()).get();
        assertAll("Verify attributes Category", () -> {
            assertEquals(categoryId.getValue(), aCategoryRetrieved.getId());
            assertEquals(aCategoryRetrieved.getName(), "Filmes");
            assertEquals(aCategoryRetrieved.getDescription(), "De arrepiar a espinha");
            assertTrue(aCategoryRetrieved.isActive());
            assertNotNull(aCategoryRetrieved.getCreatedAt());
            assertNotNull(aCategoryRetrieved.getUpdatedAt());
            assertNull(aCategoryRetrieved.getDeletedAt());
        });
    }

    @Test
    void asACatalogAdminIShouldBeAbleToActivateACategoryByItsIdentifier() throws Exception {
        //verify container and Database
        assertTrue(MY_SQL_CONTAINER.isRunning());
        assertEquals(0, this.categoryRepository.count());

        //given
        final CategoryId categoryId = this.givenCategory("Filmes", null, false);
        final UpdateCategoryRequest request =
                new UpdateCategoryRequest("Filmes", "De arrepiar a espinha", true);

        //verify database
        assertEquals(1, this.categoryRepository.count());
        final CategoryEntity entitySaved = this.categoryRepository.findById(categoryId.getValue()).get();
        assertFalse(entitySaved.isActive());
        assertNotNull(entitySaved.getDeletedAt());

        //when
        final ResultActions response = this.updateCategory(categoryId.getValue(), request);

        //then
        response
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(categoryId.getValue())));

        final CategoryEntity aCategoryRetrieved = this.categoryRepository.findById(categoryId.getValue()).get();
        assertAll("Verify attributes Category", () -> {
            assertEquals(categoryId.getValue(), aCategoryRetrieved.getId());
            assertEquals(aCategoryRetrieved.getName(), "Filmes");
            assertEquals(aCategoryRetrieved.getDescription(), "De arrepiar a espinha");
            assertTrue(aCategoryRetrieved.isActive());
            assertNotNull(aCategoryRetrieved.getCreatedAt());
            assertNotNull(aCategoryRetrieved.getUpdatedAt());
            assertNull(aCategoryRetrieved.getDeletedAt());
        });
    }

    @Test
    void asACatalogAdminIShouldBeAbleToDeactivateACategoryByItsIdentifier() throws Exception {
        //verify container and Database
        assertTrue(MY_SQL_CONTAINER.isRunning());
        assertEquals(0, this.categoryRepository.count());

        //given
        final CategoryId categoryId = this.givenCategory("Filmes", null, true);
        final UpdateCategoryRequest request =
                new UpdateCategoryRequest("Filmes", "De arrepiar a espinha", false);

        //verify database
        assertEquals(1, this.categoryRepository.count());
        final CategoryEntity entitySaved = this.categoryRepository.findById(categoryId.getValue()).get();
        assertTrue(entitySaved.isActive());
        assertNull(entitySaved.getDeletedAt());

        //when
        final ResultActions response = this.updateCategory(categoryId.getValue(), request);

        //then
        response
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(categoryId.getValue())));

        final CategoryEntity aCategoryRetrieved = this.categoryRepository.findById(categoryId.getValue()).get();
        assertAll("Verify attributes Category", () -> {
            assertEquals(categoryId.getValue(), aCategoryRetrieved.getId());
            assertEquals(aCategoryRetrieved.getName(), "Filmes");
            assertEquals(aCategoryRetrieved.getDescription(), "De arrepiar a espinha");
            assertFalse(aCategoryRetrieved.isActive());
            assertNotNull(aCategoryRetrieved.getCreatedAt());
            assertNotNull(aCategoryRetrieved.getUpdatedAt());
            assertNotNull(aCategoryRetrieved.getDeletedAt());
        });
    }

    @Test
    void asACatalogAdminIShouldBeAbleToDeleteACategoryByItsIdentifier() throws Exception {
        //verify container and Database
        assertTrue(MY_SQL_CONTAINER.isRunning());
        assertEquals(0, this.categoryRepository.count());

        //given
        final CategoryId categoryId = this.givenCategory("Filmes", "De arrepiar a espinha", true);

        //verify database
        assertEquals(1, this.categoryRepository.count());

        //when
        final ResultActions response = this.deleteACategory(categoryId.getValue());

        //then
        response
                .andExpect(status().isNoContent())
                .andExpect(header().string("Location", nullValue()));
        assertEquals(0, this.categoryRepository.count());
    }
}
