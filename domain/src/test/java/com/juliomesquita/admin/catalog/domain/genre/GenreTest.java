package com.juliomesquita.admin.catalog.domain.genre;

import com.juliomesquita.admin.catalog.domain.commom.exceptions.NotificationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Genre-Domain-Test")
class GenreTest {

    /*
     * 1. givenValidParams_whenCallsNewGenre_thenInstantiateAGenre ok
     * 2. givenInvalidParamNullName_whenCallsNewGenreAndValidate_thenReturnError
     * 3. givenInvalidParamEmptyName_whenCallsNewGenreAndValidate_thenReturnError
     * 4. givenInvalidNameWithLengthGreaterThan255_whenCallsNewGenreAndValidate_thenReturnError
     * 5. givenInvalidNameWithLengthLessThan3_whenCallsNewGenreAndValidate_thenReturnError
     * 6.
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
    void givenInvalidParamNullName_whenCallsNewGenreAndValidate_thenReturnError(){
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
    void givenInvalidParamEmptyName_whenCallsNewGenreAndValidate_thenReturnError(){
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
    void givenInvalidNameWithLengthGreaterThan255_whenCallsNewGenreAndValidate_thenReturnError(){
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
    void givenInvalidNameWithLengthLessThan3_whenCallsNewGenreAndValidate_thenReturnError(){
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


}