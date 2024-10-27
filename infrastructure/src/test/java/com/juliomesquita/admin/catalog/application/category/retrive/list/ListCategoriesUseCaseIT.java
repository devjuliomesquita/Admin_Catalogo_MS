package com.juliomesquita.admin.catalog.application.category.retrive.list;

import com.juliomesquita.admin.catalog.IntegrationTest;
import com.juliomesquita.admin.catalog.application.usecases.category.retrive.list.ListCategoriesOutput;
import com.juliomesquita.admin.catalog.application.usecases.category.retrive.list.ListCategoriesUseCase;
import com.juliomesquita.admin.catalog.domain.category.Category;
import com.juliomesquita.admin.catalog.domain.category.CategoryGateway;
import com.juliomesquita.admin.catalog.domain.commom.pagination.SearchQuery;
import com.juliomesquita.admin.catalog.domain.commom.pagination.Pagination;
import com.juliomesquita.admin.catalog.infrastructure.category.persistence.CategoryEntity;
import com.juliomesquita.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@IntegrationTest
public class ListCategoriesUseCaseIT {
    @Autowired
    private ListCategoriesUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @BeforeEach
    void mockUp() {
        final List<CategoryEntity> listCategories = Stream.of(
                        Category.newCategory("Animes", "Animes mais assistidos", true),
                        Category.newCategory("Filmes", "Filmes mais assistidos", true),
                        Category.newCategory("Series", "Series mais assistidos", true),
                        Category.newCategory("Amazon Filmes", "Filmes doa catálogo da Amazon", true),
                        Category.newCategory("Terror", "De deixar o cabelo em pé", true),
                        Category.newCategory("Horror", "Com muito sangue", true),
                        Category.newCategory("Drama", "Os mais comentados", true),
                        Category.newCategory("Suspence", null, true))
                .map(CategoryEntity::from)
                .toList();
        this.categoryRepository.saveAllAndFlush(listCategories);
    }

    @Test
    void givenAValidValidTerms_whenTermDoesNotMatchesPrePersisted_thenReturnEmptyPage() {
        //given
        final int expectedCurrentPage = 0;
        final int expectedItemsPerPage = 10;
        final String expectedTerms = "kldfjhas;lkjds;";
        final String expectedSort = "name";
        final String expectedDirection = "asc";
        final int expectedItemsCount = 0;
        final int expectedTotalItems = 0;

        final SearchQuery aQuery =
                new SearchQuery(expectedCurrentPage, expectedItemsPerPage, expectedTerms, expectedSort, expectedDirection);

        //when
        final Pagination<ListCategoriesOutput> response = this.useCase.execute(aQuery);

        //then
        assertEquals(expectedCurrentPage, response.currentPage());
        assertEquals(expectedItemsPerPage, response.itemsPerPage());
        assertEquals(expectedTotalItems, response.totalItems());
        assertEquals(expectedItemsCount, response.items().size());
    }

    @ParameterizedTest
    @CsvSource({
            "ani,0,10,1,1,Animes",
            "assistidos,0,10,3,3,Animes",
            "zon,0,10,1,1,Amazon Filmes",
            "rror,0,10,2,2,Horror",
            "Filmes,0,10,2,2,Amazon Filmes",
    })
    void givenAValidParamsTerms_whenCallsListCategories_thenReturnCategoriesFiltered(
            final String expectedTerms,
            final int expectedCurrentPage,
            final int expectedItemsPerPage,
            final long expectedTotalItems,
            final int expectedTotalItemsCount,
            final String expectedCategoryName
    ) {
        final String expectedSort = "name";
        final String expectedDirection = "asc";

        final SearchQuery aQuery =
                new SearchQuery(expectedCurrentPage, expectedItemsPerPage, expectedTerms, expectedSort, expectedDirection);

        //when
        final Pagination<ListCategoriesOutput> response = this.useCase.execute(aQuery);

        //then
        assertEquals(expectedCurrentPage, response.currentPage());
        assertEquals(expectedItemsPerPage, response.itemsPerPage());
        assertEquals(expectedTotalItems, response.totalItems());
        assertEquals(expectedTotalItemsCount, response.items().size());
        assertEquals(expectedCategoryName, response.items().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,8,8,Amazon Filmes",
            "description,desc,0,10,8,8,Series",
            "name,desc,0,10,8,8,Terror",
            "description,asc,0,10,8,8,Suspence",
    })
    void givenAValidSortAndDirection_whenCallsListCategories_thenReturnCategoriesSorted(
            final String expectedSort,
            final String expectedDirection,
            final int expectedCurrentPage,
            final int expectedItemsPerPage,
            final long expectedTotalItems,
            final int expectedTotalItemsCount,
            final String expectedCategoryName
    ) {
        final String expectedTerms = "";

        final SearchQuery aQuery =
                new SearchQuery(expectedCurrentPage, expectedItemsPerPage, expectedTerms, expectedSort, expectedDirection);

        //when
        final Pagination<ListCategoriesOutput> response = this.useCase.execute(aQuery);

        //then
        assertEquals(expectedCurrentPage, response.currentPage());
        assertEquals(expectedItemsPerPage, response.itemsPerPage());
        assertEquals(expectedTotalItems, response.totalItems());
        assertEquals(expectedTotalItemsCount, response.items().size());
        assertEquals(expectedCategoryName, response.items().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "0,3,8,3,Amazon Filmes;Animes;Drama",
            "1,3,8,3,Filmes;Horror;Series",
            "2,3,8,2,Suspence;Terror",
    })
    void givenAValidPage_whenCallsListCategories_thenReturnCategoriesPaginated(
            final int expectedCurrentPage,
            final int expectedItemsPerPage,
            final long expectedTotalItems,
            final int expectedTotalItemsCount,
            final String expectedCategoryName
    ) {
        final String expectedTerms = "";
        final String expectedSort = "name";
        final String expectedDirection = "asc";

        final SearchQuery aQuery =
                new SearchQuery(expectedCurrentPage, expectedItemsPerPage, expectedTerms, expectedSort, expectedDirection);

        //when
        final Pagination<ListCategoriesOutput> response = this.useCase.execute(aQuery);

        //then
        assertEquals(expectedCurrentPage, response.currentPage());
        assertEquals(expectedItemsPerPage, response.itemsPerPage());
        assertEquals(expectedTotalItems, response.totalItems());
        assertEquals(expectedTotalItemsCount, response.items().size());

        int index = 0;
        for (final String expectedName : expectedCategoryName.split(";")) {
            assertEquals(expectedName, response.items().get(index).name());
            index++;
        }
    }
}
