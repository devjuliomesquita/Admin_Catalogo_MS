package com.juliomesquita.admin.catalog.infrastructure.category;

import com.juliomesquita.admin.catalog.domain.category.Category;
import com.juliomesquita.admin.catalog.domain.category.CategoryId;
import com.juliomesquita.admin.catalog.domain.commom.pagination.CategorySearchQuery;
import com.juliomesquita.admin.catalog.domain.commom.pagination.Pagination;
import com.juliomesquita.admin.catalog.MySqlGatewayTest;
import com.juliomesquita.admin.catalog.infrastructure.category.persistence.CategoryEntity;
import com.juliomesquita.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@MySqlGatewayTest
class CategoryMySqlGatewayTest {
    @Autowired
    private CategoryMySqlGateway categoryMySqlGateway;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void givenAValidCategory_whenCallsCreate_thenReturnANewCategory() {
        //verify database
        assertEquals(0, this.categoryRepository.count());

        //given
        final String expectedName = "Animes";
        final String expectedDescription = "Animes de comédia";
        final boolean expectedActive = true;
        final Category aCategory = Category.newCategory(expectedName, expectedDescription, expectedActive);

        //when
        final Category aCategoryCreated = this.categoryMySqlGateway.create(aCategory);

        //then
        assertEquals(1, this.categoryRepository.count());
        assertAll("Verify category created - level use case", () -> {
            assertEquals(aCategory.getId(), aCategoryCreated.getId());
            assertEquals(expectedName, aCategoryCreated.getName());
            assertEquals(expectedDescription, aCategoryCreated.getDescription());
            assertEquals(expectedActive, aCategoryCreated.isActive());
            assertEquals(aCategory.getCreatedAt(), aCategoryCreated.getCreatedAt());
            assertEquals(aCategory.getUpdatedAt(), aCategoryCreated.getUpdatedAt());
            assertNull(aCategoryCreated.getDeletedAt());
        });

        //second when
        final CategoryEntity categoryEntityCreated =
                this.categoryRepository.findById(aCategoryCreated.getId().getValue()).get();

        //second then
        assertAll("Verify category created - level repository", () -> {
            assertEquals(aCategory.getId().getValue(), categoryEntityCreated.getId());
            assertEquals(expectedName, categoryEntityCreated.getName());
            assertEquals(expectedDescription, categoryEntityCreated.getDescription());
            assertEquals(expectedActive, categoryEntityCreated.isActive());
            assertEquals(aCategory.getCreatedAt(), categoryEntityCreated.getCreatedAt());
            assertEquals(aCategory.getUpdatedAt(), categoryEntityCreated.getUpdatedAt());
            assertNull(categoryEntityCreated.getDeletedAt());
        });
    }

    @Test
    void givenAValidCategory_whenCallsUpdate_thenReturnCategoryUpdated() {
        //verify database
        assertEquals(0, this.categoryRepository.count());

        //given
        final String expectedName = "Animes";
        final String expectedDescription = "Animes de comédia";
        final boolean expectedActive = true;
        final Category aCategory = Category.newCategory(expectedName, null, expectedActive);
        this.categoryRepository.saveAndFlush(CategoryEntity.from(aCategory));
        final Category aCategoryUpdated = aCategory
                .clone()
                .update(expectedName, expectedDescription, expectedActive);

        //first then
        assertEquals(1, this.categoryRepository.count());

        //when
        final Category aCategorySaved = this.categoryMySqlGateway.update(aCategoryUpdated);

        //second then
        assertEquals(1, this.categoryRepository.count());
        assertAll("Verify category updated - level use case", () -> {
            assertEquals(aCategory.getId(), aCategorySaved.getId());
            assertEquals(expectedName, aCategorySaved.getName());
            assertEquals(expectedDescription, aCategorySaved.getDescription());
            assertEquals(expectedActive, aCategorySaved.isActive());
            assertEquals(aCategory.getCreatedAt(), aCategorySaved.getCreatedAt());
            assertNotEquals(aCategory.getUpdatedAt(),aCategorySaved.getUpdatedAt());
            assertNull(aCategorySaved.getDeletedAt());
        });

        //second when
        final CategoryEntity categoryEntityUpdated =
                this.categoryRepository.findById(aCategorySaved.getId().getValue()).get();

        //third then
        assertAll("Verify category updated - level repository", () -> {
            assertEquals(aCategory.getId().getValue(), categoryEntityUpdated.getId());
            assertEquals(expectedName, categoryEntityUpdated.getName());
            assertEquals(expectedDescription, categoryEntityUpdated.getDescription());
            assertEquals(expectedActive, categoryEntityUpdated.isActive());
            assertEquals(aCategory.getCreatedAt(), categoryEntityUpdated.getCreatedAt());
            assertTrue(aCategory.getUpdatedAt().isBefore(aCategorySaved.getUpdatedAt()));
            assertNull(categoryEntityUpdated.getDeletedAt());
        });
    }

    @Test
    void givenAPrePersistedCategoryAndValidCategory_whenCallsDelete_thenDeleteCategory() {
        //verify database
        assertEquals(0, this.categoryRepository.count());

        //given
        final Category aCategory = Category.newCategory("Si-Fi", "Alucinantes", true);
        this.categoryRepository.saveAndFlush(CategoryEntity.from(aCategory));

        //first then
        assertEquals(1, this.categoryRepository.count());

        //when
        this.categoryMySqlGateway.deleteById(aCategory.getId());

        //second then
        assertEquals(0, this.categoryRepository.count());
    }

    @Test
    void givenInvalidCategoryId_whenCallsDelete_thenDeleteCategory() {
        //verify database
        assertEquals(0, this.categoryRepository.count());

        //when
        this.categoryMySqlGateway.deleteById(CategoryId.from(UUID.randomUUID()));

        //then
        assertEquals(0, this.categoryRepository.count());
    }

    @Test
    void givenAPrePersistedCategoryAndValidCategoryId_whenCallsFindById_thenReturnCategory() {
        //verify database
        assertEquals(0, this.categoryRepository.count());

        //given
        final Category aCategory = Category.newCategory("Si-Fi", "Alucinantes", true);
        this.categoryRepository.saveAndFlush(CategoryEntity.from(aCategory));

        //first then
        assertEquals(1, this.categoryRepository.count());

        //when
        final Category aCategoryOpt = this.categoryMySqlGateway.findById(aCategory.getId()).get();

        //second then
        assertEquals(1, this.categoryRepository.count());
        assertAll("Verify category find - level use case", () -> {
            assertEquals(aCategory.getId(), aCategoryOpt.getId());
            assertEquals(aCategory.getName(), aCategoryOpt.getName());
            assertEquals(aCategory.getDescription(), aCategoryOpt.getDescription());
            assertEquals(aCategory.isActive(), aCategoryOpt.isActive());
            assertEquals(aCategory.getCreatedAt(), aCategoryOpt.getCreatedAt());
            assertEquals(aCategory.getUpdatedAt(), aCategoryOpt.getUpdatedAt());
            assertEquals(aCategory.getDeletedAt(), aCategoryOpt.getDeletedAt());
        });
    }

    @Test
    void givenValidCategoryIdNotStored_whenCallsFindById_thenReturnEmpty() {
        //verify database
        assertEquals(0, this.categoryRepository.count());

        //when
        final Optional<Category> aCategoryOpt = this.categoryMySqlGateway.findById(CategoryId.from(UUID.randomUUID()));

        //then
        assertTrue(aCategoryOpt.isEmpty());
    }

    @Test
    void givenPrePersistedCategories_whenCallsFindAll_thenReturnCategoriesPaginated() {
        //verify database
        assertEquals(0, this.categoryRepository.count());

        //expected
        final int expectedCurrentPage = 0;
        final int expectedItemsPerPage = 4;
        final int expectedTotalItems = 3;

        //given
        final Category filmes = Category.newCategory("Filmes", null, true);
        final Category series = Category.newCategory("Series", null, true);
        final Category animes = Category.newCategory("Animes", null, true);
        final CategorySearchQuery categorySearchQuery =
                new CategorySearchQuery(expectedCurrentPage, expectedItemsPerPage, "", "createdAt", "asc");
        this.categoryRepository.saveAllAndFlush(List.of(
                CategoryEntity.from(filmes),
                CategoryEntity.from(series),
                CategoryEntity.from(animes))
        );

        //first then
        assertEquals(expectedTotalItems, this.categoryRepository.count());

        //when
        final Pagination<Category> categoryPagination = this.categoryMySqlGateway.findAll(categorySearchQuery);

        //second then
        assertEquals(expectedCurrentPage, categoryPagination.currentPage());
        assertEquals(expectedItemsPerPage, categoryPagination.itemsPerPage());
        assertEquals(expectedTotalItems, categoryPagination.totalItems());
        assertEquals(filmes.getName(), categoryPagination.items().get(0).getName());
        assertEquals(series.getName(), categoryPagination.items().get(1).getName());
        assertEquals(animes.getName(), categoryPagination.items().get(2).getName());
    }

    @Test
    void givenEmptyCategoriesTable_whenCallsFindAll_thenReturnEmptyPaginated() {
        //verify database
        assertEquals(0, this.categoryRepository.count());

        //expected
        final int expectedCurrentPage = 0;
        final int expectedItemsPerPage = 4;
        final int expectedTotalItems = 0;

        //given
        final CategorySearchQuery categorySearchQuery =
                new CategorySearchQuery(expectedCurrentPage, expectedItemsPerPage, "", "createdAt", "asc");

        //when
        final Pagination<Category> categoryPagination = this.categoryMySqlGateway.findAll(categorySearchQuery);

        //second then
        assertEquals(expectedCurrentPage, categoryPagination.currentPage());
        assertEquals(expectedItemsPerPage, categoryPagination.itemsPerPage());
        assertEquals(expectedTotalItems, categoryPagination.totalItems());
    }

    @Test
    void givenFollowPaginated_whenCallsFindAllWithPage1_thenReturnPaginated() {
        //verify database
        assertEquals(0, this.categoryRepository.count());

        //expected
        final int expectedItemsPerPage = 1;
        final int expectedTotalItems = 3;

        //given
        final Category filmes = Category.newCategory("Filmes", null, true);
        final Category series = Category.newCategory("Series", null, true);
        final Category animes = Category.newCategory("Animes", null, true);
        this.categoryRepository.saveAllAndFlush(List.of(
                CategoryEntity.from(filmes),
                CategoryEntity.from(series),
                CategoryEntity.from(animes))
        );

        //first then
        assertEquals(expectedTotalItems, this.categoryRepository.count());

        //first when
        final CategorySearchQuery categorySearchQueryFirst =
                new CategorySearchQuery(0, expectedItemsPerPage, "", "createdAt", "asc");
        final Pagination<Category> categoryPaginationFirst = this.categoryMySqlGateway.findAll(categorySearchQueryFirst);

        //then - response 1
        assertEquals(0, categoryPaginationFirst.currentPage());
        assertEquals(expectedItemsPerPage, categoryPaginationFirst.itemsPerPage());
        assertEquals(expectedTotalItems, categoryPaginationFirst.totalItems());
        assertEquals(filmes.getName(), categoryPaginationFirst.items().get(0).getName());

        //second when
        final CategorySearchQuery categorySearchQuerySecond =
                new CategorySearchQuery(2, expectedItemsPerPage, "", "name", "asc");
        final Pagination<Category> categoryPaginationSecond = this.categoryMySqlGateway.findAll(categorySearchQuerySecond);

        //then - response 2
        assertEquals(2, categoryPaginationSecond.currentPage());
        assertEquals(expectedItemsPerPage, categoryPaginationSecond.itemsPerPage());
        assertEquals(expectedTotalItems, categoryPaginationSecond.totalItems());
        assertEquals(series.getName(), categoryPaginationSecond.items().get(0).getName());

        //third when
        final CategorySearchQuery categorySearchQueryThird =
                new CategorySearchQuery(0, expectedItemsPerPage, "ani", "name", "asc");
        final Pagination<Category> categoryPaginationThird = this.categoryMySqlGateway.findAll(categorySearchQueryThird);

        //then - response 3
        assertEquals(0, categoryPaginationThird.currentPage());
        assertEquals(expectedItemsPerPage, categoryPaginationThird.itemsPerPage());
        assertEquals(1, categoryPaginationThird.totalItems());
        assertEquals(animes.getName(), categoryPaginationThird.items().get(0).getName());
    }
}