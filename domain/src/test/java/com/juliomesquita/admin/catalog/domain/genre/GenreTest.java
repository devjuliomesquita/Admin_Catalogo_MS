package com.juliomesquita.admin.catalog.domain.genre;

import com.juliomesquita.admin.catalog.domain.category.CategoryId;
import com.juliomesquita.admin.catalog.domain.commom.exceptions.NotificationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Genre-Domain-Test")
class GenreTest {

    /*
     * 1. givenValidParams_whenCallsNewGenre_thenInstantiateAGenre - ok
     * 2. givenInvalidParamNullName_whenCallsNewGenreAndValidate_thenReturnError - ok
     * 3. givenInvalidParamEmptyName_whenCallsNewGenreAndValidate_thenReturnError - ok
     * 4. givenInvalidNameWithLengthGreaterThan255_whenCallsNewGenreAndValidate_thenReturnError - ok
     * 5. givenInvalidNameWithLengthLessThan3_whenCallsNewGenreAndValidate_thenReturnError - ok
     * 6. givenAnActiveGenre_whenCallsDeactivate_thenReturnOk - ok
     * 7. givenAnInactiveGenre_whenCallsActivate_thenReturnOk - ok
     * 8. givenAValidGenreInactive_whenCallsUpdate_thenReturnGenreActiveUpdated - ok
     * 9. givenAValidGenreActive_whenCallsUpdate_thenReturnGenreInactiveUpdated - ok
     * 10. givenInvalidParamNullName_whenCallsUpdate_thenReturnNotificationError - ok
     * 11. givenInvalidParamEmptyName_whenCallsUpdate_thenReturnNotificationError - ok
     * 12. givenInvalidNameWithLengthGreaterThan255_whenCallsUpdate_thenReturnNotificationError  - ok
     * 13. givenInvalidNameWithLengthLessThan3_whenCallsUpdate_thenReturnNotificationError - ok
     * 14. givenAValidGenreWithParamCategoriesNull_whenCallsUpdate_thenReturnOK - ok
     * 15. givenAValidGenreWithCategoryIdValid_whenCallsAddCategories_thenReturnOk
     * 16. givenAValidGenreWithCategoryIdNull_whenCallsAddCategories_thenReturnOk
     * 17. givenAValidGenreWithCategoryIdValid_whenCallsRemoveCategories_thenReturnOk
     * 18. givenAValidGenreWithCategoryIdNull_whenCallsRemoveCategories_thenReturnOk
     * */

    @Test
    void givenValidParams_whenCallsNewGenre_thenInstantiateAGenre() {
        //given
        final String expectedName = "Infantil";
        final boolean expectedActive = true;
        final int expectedCategoriesCount = 0;

        //when
        final Genre aGenre = Genre.newGenre(expectedName, expectedActive);

        //then
        assertAll("Verify attributes of Genre", () -> {
            assertNotNull(aGenre);
            assertNotNull(aGenre.getId());
            assertEquals(expectedName, aGenre.getName());
            assertEquals(expectedActive, aGenre.isActive());
            assertNotNull(aGenre.getCategories());
            assertEquals(expectedCategoriesCount, aGenre.getCategories().size());
            assertNotNull(aGenre.getCreatedAt());
            assertNotNull(aGenre.getUpdatedAt());
            assertNull(aGenre.getDeletedAt());
        });
    }

    @Test
    void givenInvalidParamNullName_whenCallsNewGenreAndValidate_thenReturnError() {
        //given
        final String expectedName = null;
        final boolean expectedActive = true;
        final String expectedErrorMessageTitle = "Failed to create a Aggregate Genre.";
        final String expectedErrorMessage = "'name' should be not null";
        final int expectedErrorCount = 1;

        //when
        final NotificationException exception =
                assertThrows(NotificationException.class, () -> Genre.newGenre(expectedName, expectedActive));

        //then
        assertEquals(expectedErrorMessageTitle, exception.getMessage());
        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @Test
    void givenInvalidParamEmptyName_whenCallsNewGenreAndValidate_thenReturnError() {
        //given
        final String expectedName = "";
        final boolean expectedActive = true;
        final String expectedErrorMessageTitle = "Failed to create a Aggregate Genre.";
        final String expectedErrorMessage = "'name' should be not empty";
        final int expectedErrorCount = 1;

        //when
        final NotificationException exception =
                assertThrows(NotificationException.class, () -> Genre.newGenre(expectedName, expectedActive));

        //then
        assertEquals(expectedErrorMessageTitle, exception.getMessage());
        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @Test
    void givenInvalidNameWithLengthGreaterThan255_whenCallsNewGenreAndValidate_thenReturnError() {
        //given
        final String expectedName = """
                givenInvalidNameWithLengthGreaterThan255_whenCallsNewGenreAndValidate_thenReturnError
                givenInvalidNameWithLengthGreaterThan255_whenCallsNewGenreAndValidate_thenReturnError
                givenInvalidNameWithLengthGreaterThan255_whenCallsNewGenreAndValidate_thenReturnError
                givenInvalidNameWithLengthGreaterThan255_whenCallsNewGenreAndValidate_thenReturnError
                """;
        final boolean expectedActive = true;
        final String expectedErrorMessageTitle = "Failed to create a Aggregate Genre.";
        final String expectedErrorMessage = "'name' must be between 3 and 255 characters";
        final int expectedErrorCount = 1;

        //when
        final NotificationException exception =
                assertThrows(NotificationException.class, () -> Genre.newGenre(expectedName, expectedActive));

        //then
        assertEquals(expectedErrorMessageTitle, exception.getMessage());
        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @Test
    void givenInvalidNameWithLengthLessThan3_whenCallsNewGenreAndValidate_thenReturnError() {
        //given
        final String expectedName = "     ab      ";
        final boolean expectedActive = true;
        final String expectedErrorMessageTitle = "Failed to create a Aggregate Genre.";
        final String expectedErrorMessage = "'name' must be between 3 and 255 characters";
        final int expectedErrorCount = 1;

        //when
        final NotificationException exception =
                assertThrows(NotificationException.class, () -> Genre.newGenre(expectedName, expectedActive));

        //then
        assertEquals(expectedErrorMessageTitle, exception.getMessage());
        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @Test
    void givenAnActiveGenre_whenCallsDeactivate_thenReturnOk() throws InterruptedException {
        //given
        final String expectedName = "Infantil";
        final boolean expectedActive = false;
        final int expectedCategoriesCount = 0;
        final Genre aGenre = Genre.newGenre(expectedName, true);
        final Instant expectedUpdatedAt = aGenre.getUpdatedAt();

        assertTrue(aGenre.isActive());

        //when
        Thread.sleep(1);
        aGenre.deactivate();

        //then
        assertAll("Verify attributes of Genre", () -> {
            assertNotNull(aGenre);
            assertNotNull(aGenre.getId());
            assertEquals(expectedName, aGenre.getName());
            assertEquals(expectedActive, aGenre.isActive());
            assertNotNull(aGenre.getCategories());
            assertEquals(expectedCategoriesCount, aGenre.getCategories().size());
            assertNotNull(aGenre.getCreatedAt());
            assertTrue(expectedUpdatedAt.isBefore(aGenre.getUpdatedAt()));
            assertNotNull(aGenre.getDeletedAt());
        });
    }

    @Test
    void givenAnInactiveGenre_whenCallsActivate_thenReturnOk() throws InterruptedException {
        //given
        final String expectedName = "Infantil";
        final boolean expectedActive = true;
        final int expectedCategoriesCount = 0;
        final Genre aGenre = Genre.newGenre(expectedName, false);
        final Instant expectedUpdatedAt = aGenre.getUpdatedAt();

        assertFalse(aGenre.isActive());

        //when
        Thread.sleep(1);
        aGenre.activate();

        //then
        assertAll("Verify attributes of Genre", () -> {
            assertNotNull(aGenre);
            assertNotNull(aGenre.getId());
            assertEquals(expectedName, aGenre.getName());
            assertEquals(expectedActive, aGenre.isActive());
            assertNotNull(aGenre.getCategories());
            assertEquals(expectedCategoriesCount, aGenre.getCategories().size());
            assertNotNull(aGenre.getCreatedAt());
            assertTrue(expectedUpdatedAt.isBefore(aGenre.getUpdatedAt()));
            assertNull(aGenre.getDeletedAt());
        });
    }

    @Test
    void givenAValidGenreInactive_whenCallsUpdate_thenReturnGenreActiveUpdated() throws InterruptedException {
        //given
        final String expectedName = "Infantil";
        final boolean expectedActive = true;
        final int expectedCategoriesCount = 2;
        final CategoryId categoryFilms = CategoryId.unique();
        final CategoryId categorySeries = CategoryId.unique();
        final List<CategoryId> expectedListCategories = List.of(categoryFilms, categorySeries);
        final Genre aGenre = Genre.newGenre("Juvenil", false);
        final Instant expectedUpdateAt = aGenre.getUpdatedAt();

        //first then
        assertFalse(aGenre.isActive());
        assertEquals(0, aGenre.getCategories().size());

        //when
        Thread.sleep(1);
        final Genre aGenreUpdated = aGenre.update(expectedName, expectedActive, expectedListCategories);

        //then
        assertAll("Verify attributes of Genre", () -> {
            assertNotNull(aGenreUpdated);
            assertEquals(aGenre.getId(), aGenreUpdated.getId());
            assertEquals(expectedName, aGenreUpdated.getName());
            assertEquals(expectedActive, aGenreUpdated.isActive());
            assertEquals(expectedCategoriesCount, aGenreUpdated.getCategories().size());
            assertEquals(expectedListCategories, aGenreUpdated.getCategories());
            assertEquals(expectedListCategories.get(0), aGenreUpdated.getCategories().get(0));
            assertEquals(expectedListCategories.get(1), aGenreUpdated.getCategories().get(1));
            assertEquals(aGenre.getCreatedAt(), aGenreUpdated.getCreatedAt());
            assertTrue(expectedUpdateAt.isBefore(aGenreUpdated.getUpdatedAt()));
            assertNull(aGenreUpdated.getDeletedAt());
        });
    }

    @Test
    void givenAValidGenreActive_whenCallsUpdate_thenReturnGenreInactiveUpdated() throws InterruptedException {
        //given
        final String expectedName = "Infantil";
        final boolean expectedActive = false;
        final int expectedCategoriesCount = 2;
        final CategoryId categoryFilms = CategoryId.unique();
        final CategoryId categorySeries = CategoryId.unique();
        final List<CategoryId> expectedListCategories = List.of(categoryFilms, categorySeries);
        final Genre aGenre = Genre.newGenre("Juvenil", true);
        final Instant expectedUpdateAt = aGenre.getUpdatedAt();

        //first then
        assertTrue(aGenre.isActive());
        assertEquals(0, aGenre.getCategories().size());

        //when
        Thread.sleep(1);
        final Genre aGenreUpdated = aGenre.update(expectedName, expectedActive, expectedListCategories);

        //then
        assertAll("Verify attributes of Genre", () -> {
            assertNotNull(aGenreUpdated);
            assertEquals(aGenre.getId(), aGenreUpdated.getId());
            assertEquals(expectedName, aGenreUpdated.getName());
            assertEquals(expectedActive, aGenreUpdated.isActive());
            assertEquals(expectedCategoriesCount, aGenreUpdated.getCategories().size());
            assertEquals(expectedListCategories, aGenreUpdated.getCategories());
            assertEquals(expectedListCategories.get(0), aGenreUpdated.getCategories().get(0));
            assertEquals(expectedListCategories.get(1), aGenreUpdated.getCategories().get(1));
            assertEquals(aGenre.getCreatedAt(), aGenreUpdated.getCreatedAt());
            assertTrue(expectedUpdateAt.isBefore(aGenreUpdated.getUpdatedAt()));
            assertNotNull(aGenreUpdated.getDeletedAt());
        });
    }

    @Test
    void givenAValidGenreActiveWithListCategoriesNull_whenCallsUpdate_thenReturnGenre() throws InterruptedException {
        //given
        final String expectedName = "Infantil";
        final boolean expectedActive = false;
        final int expectedCategoriesCount = 0;
        final List<CategoryId> expectedListCategories = Collections.emptyList();
        final Genre aGenre = Genre.newGenre("Juvenil", true);
        final Instant expectedUpdateAt = aGenre.getUpdatedAt();

        //first then
        assertTrue(aGenre.isActive());
        assertEquals(0, aGenre.getCategories().size());

        //when
        Thread.sleep(1);
        final Genre aGenreUpdated = aGenre.update(expectedName, expectedActive, null);

        //then
        assertAll("Verify attributes of Genre", () -> {
            assertNotNull(aGenreUpdated);
            assertEquals(aGenre.getId(), aGenreUpdated.getId());
            assertEquals(expectedName, aGenreUpdated.getName());
            assertEquals(expectedActive, aGenreUpdated.isActive());
            assertEquals(expectedCategoriesCount, aGenreUpdated.getCategories().size());
            assertEquals(expectedListCategories, aGenreUpdated.getCategories());
            assertEquals(aGenre.getCreatedAt(), aGenreUpdated.getCreatedAt());
            assertTrue(expectedUpdateAt.isBefore(aGenreUpdated.getUpdatedAt()));
            assertNotNull(aGenreUpdated.getDeletedAt());
        });
    }

    @Test
    void givenInvalidParamNullName_whenCallsUpdate_thenReturnNotificationError(){
        //given
        final String expectedName = null;
        final boolean expectedActive = true;
        final String expectedErrorMessageTitle = "Failed to create a Aggregate Genre.";
        final String expectedErrorMessage = "'name' should be not null";
        final int expectedErrorCount = 1;
        final CategoryId categoryFilms = CategoryId.unique();
        final CategoryId categorySeries = CategoryId.unique();
        final List<CategoryId> expectedListCategories = List.of(categoryFilms, categorySeries);
        final Genre aGenre = Genre.newGenre("Juvenil", false);

        //first then
        assertFalse(aGenre.isActive());
        assertEquals(0, aGenre.getCategories().size());

        //when
        final NotificationException exception =
                assertThrows(NotificationException.class, () -> aGenre.update(expectedName, expectedActive, expectedListCategories));

        //then
        assertEquals(expectedErrorMessageTitle, exception.getMessage());
        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @Test
    void givenInvalidParamEmptyName_whenCallsUpdate_thenReturnNotificationError(){
        //given
        final String expectedName = "";
        final boolean expectedActive = true;
        final String expectedErrorMessageTitle = "Failed to create a Aggregate Genre.";
        final String expectedErrorMessage = "'name' should be not empty";
        final int expectedErrorCount = 1;
        final CategoryId categoryFilms = CategoryId.unique();
        final CategoryId categorySeries = CategoryId.unique();
        final List<CategoryId> expectedListCategories = List.of(categoryFilms, categorySeries);
        final Genre aGenre = Genre.newGenre("Juvenil", false);

        //first then
        assertFalse(aGenre.isActive());
        assertEquals(0, aGenre.getCategories().size());

        //when
        final NotificationException exception =
                assertThrows(NotificationException.class, () -> aGenre.update(expectedName, expectedActive, expectedListCategories));

        //then
        assertEquals(expectedErrorMessageTitle, exception.getMessage());
        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @Test
    void givenInvalidNameWithLengthGreaterThan255_whenCallsUpdate_thenReturnNotificationError(){
        //given
        final String expectedName = """
                givenInvalidNameWithLengthGreaterThan255_whenCallsUpdate_thenReturnNotificationError
                givenInvalidNameWithLengthGreaterThan255_whenCallsUpdate_thenReturnNotificationError
                givenInvalidNameWithLengthGreaterThan255_whenCallsUpdate_thenReturnNotificationError
                givenInvalidNameWithLengthGreaterThan255_whenCallsUpdate_thenReturnNotificationError
                """;
        final boolean expectedActive = true;
        final String expectedErrorMessageTitle = "Failed to create a Aggregate Genre.";
        final String expectedErrorMessage = "'name' must be between 3 and 255 characters";
        final int expectedErrorCount = 1;
        final CategoryId categoryFilms = CategoryId.unique();
        final CategoryId categorySeries = CategoryId.unique();
        final List<CategoryId> expectedListCategories = List.of(categoryFilms, categorySeries);
        final Genre aGenre = Genre.newGenre("Juvenil", false);

        //first then
        assertFalse(aGenre.isActive());
        assertEquals(0, aGenre.getCategories().size());

        //when
        final NotificationException exception =
                assertThrows(NotificationException.class, () -> aGenre.update(expectedName, expectedActive, expectedListCategories));

        //then
        assertEquals(expectedErrorMessageTitle, exception.getMessage());
        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @Test
    void givenInvalidNameWithLengthLessThan3_whenCallsUpdate_thenReturnNotificationError(){
        //given
        final String expectedName = "    as    ";
        final boolean expectedActive = true;
        final String expectedErrorMessageTitle = "Failed to create a Aggregate Genre.";
        final String expectedErrorMessage = "'name' must be between 3 and 255 characters";
        final int expectedErrorCount = 1;
        final CategoryId categoryFilms = CategoryId.unique();
        final CategoryId categorySeries = CategoryId.unique();
        final List<CategoryId> expectedListCategories = List.of(categoryFilms, categorySeries);
        final Genre aGenre = Genre.newGenre("Juvenil", false);

        //first then
        assertFalse(aGenre.isActive());
        assertEquals(0, aGenre.getCategories().size());

        //when
        final NotificationException exception =
                assertThrows(NotificationException.class, () -> aGenre.update(expectedName, expectedActive, expectedListCategories));

        //then
        assertEquals(expectedErrorMessageTitle, exception.getMessage());
        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }
}