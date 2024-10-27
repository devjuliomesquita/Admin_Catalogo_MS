package com.juliomesquita.admin.catalog.application.usecases.category.retrive.list;

import com.juliomesquita.admin.catalog.domain.category.Category;
import com.juliomesquita.admin.catalog.domain.category.CategoryGateway;
import com.juliomesquita.admin.catalog.domain.commom.pagination.SearchQuery;
import com.juliomesquita.admin.catalog.domain.commom.pagination.Pagination;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultListCategoriesUseCaseTest {
    @InjectMocks
    private DefaultListCategoriesUseCase useCase;
    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(categoryGateway);
    }

    @Test
    void givenAValidQuery_whenCallsListCategories_thenReturnListCategory() {
        //given
        final List<Category> categories = List.of(
                Category.newCategory("Series", null, true),
                Category.newCategory("Filmes", null, true));
        final int expectedCurrentPage = 0;
        final int expectedItemsPerPage = 2;
        final String expectedTerms = "";
        final String expectedSort = "createdAt";
        final String expectedDirection = "ASC";
        final long expectedTotalItems = categories.size();
        final int expectedTotalPages = (int) expectedTotalItems / expectedItemsPerPage;
        final SearchQuery aQuery =
                new SearchQuery(expectedCurrentPage, expectedItemsPerPage, expectedTerms, expectedSort, expectedDirection);
        final Pagination<Category> expectedPagination =
                new Pagination<>(categories, expectedCurrentPage, expectedItemsPerPage, categories.size(), expectedTotalPages);

        //when
        when(this.categoryGateway.findAll(eq(aQuery))).thenReturn(expectedPagination);
        final Pagination<ListCategoriesOutput> response = this.useCase.execute(aQuery);

        //then
        assertAll("Verify fields Response", () -> {
            assertEquals(expectedCurrentPage, response.currentPage());
            assertEquals(expectedItemsPerPage, response.itemsPerPage());
            assertEquals(expectedTotalItems, response.totalItems());
            assertEquals(expectedTotalPages, response.totalPages());
            assertEquals(categories.get(0).getId().getValue(), response.items().get(0).id());
            assertEquals(categories.get(1).getId().getValue(), response.items().get(1).id());
        });
        verify(this.categoryGateway, times(1)).findAll(eq(aQuery));
    }

    @Test
    void givenAValidQuery_whenCallsListCategories_thenReturnListEmptyCategory() {
        //given
        final List<Category> categories = List.of();
        final int expectedCurrentPage = 0;
        final int expectedItemsPerPage = 2;
        final String expectedTerms = "";
        final String expectedSort = "createdAt";
        final String expectedDirection = "ASC";
        final long expectedTotalItems = categories.size();
        final int expectedTotalPages = expectedTotalItems == 0 ? (int) expectedTotalItems :  (int) expectedTotalItems / expectedItemsPerPage;
        final SearchQuery aQuery =
                new SearchQuery(expectedCurrentPage, expectedItemsPerPage, expectedTerms, expectedSort, expectedDirection);
        final Pagination<Category> expectedPagination =
                new Pagination<>(categories, expectedCurrentPage, expectedItemsPerPage, categories.size(), expectedTotalPages);

        //when
        when(this.categoryGateway.findAll(eq(aQuery))).thenReturn(expectedPagination);
        final Pagination<ListCategoriesOutput> response = this.useCase.execute(aQuery);

        //then
        assertAll("Verify fields Response", () -> {
            assertEquals(expectedCurrentPage, response.currentPage());
            assertEquals(expectedItemsPerPage, response.itemsPerPage());
            assertEquals(expectedTotalItems, response.totalItems());
            assertEquals(expectedTotalPages, response.totalPages());
        });
        verify(this.categoryGateway, times(1)).findAll(eq(aQuery));
    }

    @Test
    void givenAValidQuery_whenCallsListCategories_thenReturnAnyException() {
        //given
        final int expectedCurrentPage = 0;
        final int expectedItemsPerPage = 2;
        final String expectedTerms = "";
        final String expectedSort = "createdAt";
        final String expectedDirection = "ASC";
        final String expectedMessageError = "Gateway error";
        final SearchQuery aQuery =
                new SearchQuery(expectedCurrentPage, expectedItemsPerPage, expectedTerms, expectedSort, expectedDirection);

        //when
        when(this.categoryGateway.findAll(eq(aQuery)))
                .thenThrow(new IllegalArgumentException(expectedMessageError));

        //then
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> this.useCase.execute(aQuery));
        assertEquals(expectedMessageError, exception.getMessage());
        verify(this.categoryGateway, times(1)).findAll(eq(aQuery));
    }

}