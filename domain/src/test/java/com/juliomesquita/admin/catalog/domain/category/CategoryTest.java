package com.juliomesquita.admin.catalog.domain.category;

import com.juliomesquita.admin.catalog.domain.commom.exceptions.DomainException;
import com.juliomesquita.admin.catalog.domain.commom.validation.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void givenAValidParams_whenCallNewCategory_thenInstantiateACategory() {
        //given
        final String expectedName = "Series";
        final String expectedDescription = "Series mais assistidas";
        final boolean expectedIsActive = true;

        //when
        final Category aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        //then
        Assertions.assertNotNull(aCategory);
        Assertions.assertNotNull(aCategory.getId());
        Assertions.assertNotNull(aCategory.getCreatedAt());
        Assertions.assertNotNull(aCategory.getUpdatedAt());
        Assertions.assertNull(aCategory.getDeletedAt());
        Assertions.assertEquals(aCategory.getName(), expectedName);
        Assertions.assertEquals(aCategory.getDescription(), expectedDescription);
        Assertions.assertEquals(aCategory.isActive(), expectedIsActive);
    }

    @Test
    void givenAnInvalidNullName_whenCallNewCategoryAndValidate_thenShouldError() {
        //given
        final String expectedName = null;
        final String expectedMessageError = "'name' should be not null";
        final int expectedMessageCount = 1;
        final String expectedDescription = "Series mais assistidas";
        final boolean expectedIsActive = true;

        //when
        final Category aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final var actualException =
                Assertions.assertThrows(DomainException.class, () -> aCategory.validate(new ThrowsValidationHandler()));

        //then
        Assertions.assertEquals(actualException.getErrors().get(0).message(), expectedMessageError);
        Assertions.assertEquals(actualException.getErrors().size(), expectedMessageCount);
    }

    @Test
    void givenAnInvalidEmptyName_whenCallNewCategoryAndValidate_thenShouldError() {
        //given
        final String expectedName = " ";
        final String expectedMessageError = "'name' should be not empty";
        final int expectedMessageCount = 1;
        final String expectedDescription = "Series mais assistidas";
        final boolean expectedIsActive = true;

        //when
        final Category aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final var actualException =
                Assertions.assertThrows(DomainException.class, () -> aCategory.validate(new ThrowsValidationHandler()));

        //then
        Assertions.assertEquals(actualException.getErrors().get(0).message(), expectedMessageError);
        Assertions.assertEquals(actualException.getErrors().size(), expectedMessageCount);
    }

    @Test
    void givenAnInvalidNameLengthLessThan3_whenCallNewCategoryAndValidate_thenShouldError() {
        //given
        final String expectedName = "ab ";
        final String expectedMessageError = "'name' must be between 3 and 255 characters";
        final int expectedMessageCount = 1;
        final String expectedDescription = "Series mais assistidas";
        final boolean expectedIsActive = true;

        //when
        final Category aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final var actualException =
                Assertions.assertThrows(DomainException.class, () -> aCategory.validate(new ThrowsValidationHandler()));

        //then
        Assertions.assertEquals(actualException.getErrors().get(0).message(), expectedMessageError);
        Assertions.assertEquals(actualException.getErrors().size(), expectedMessageCount);
    }

    @Test
    void givenAnInvalidNameLengthMoreThan255_whenCallNewCategoryAndValidate_thenShouldError() {
        //given
        final String expectedName = """
                É claro que o consenso sobre a necessidade de qualificação garante a contribuição de um grupo importante 
                na determinação do sistema de participação geral. Por outro lado, a complexidade dos estudos efetuados 
                afeta positivamente a correta previsão do processo de comunicação como um todo.
                """;
        final String expectedMessageError = "'name' must be between 3 and 255 characters";
        final int expectedMessageCount = 1;
        final String expectedDescription = "Series mais assistidas";
        final boolean expectedIsActive = true;

        //when
        final Category aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final var actualException =
                Assertions.assertThrows(DomainException.class, () -> aCategory.validate(new ThrowsValidationHandler()));

        //then
        Assertions.assertEquals(actualException.getErrors().get(0).message(), expectedMessageError);
        Assertions.assertEquals(actualException.getErrors().size(), expectedMessageCount);
    }

    @Test
    void givenAValidEmptyDescription_whenCallNewCategory_thenInstantiateACategory() {
        //given
        final String expectedName = "Series";
        final String expectedDescription = " ";
        final boolean expectedIsActive = true;

        //when
        final Category aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        //then
        Assertions.assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));
        Assertions.assertNotNull(aCategory);
        Assertions.assertNotNull(aCategory.getId());
        Assertions.assertNotNull(aCategory.getCreatedAt());
        Assertions.assertNotNull(aCategory.getUpdatedAt());
        Assertions.assertNull(aCategory.getDeletedAt());
        Assertions.assertEquals(aCategory.getName(), expectedName);
        Assertions.assertEquals(aCategory.getDescription(), expectedDescription);
        Assertions.assertEquals(aCategory.isActive(), expectedIsActive);
    }

    @Test
    void givenAValidFalseIsActive_whenCallNewCategory_thenInstantiateACategory() {
        //given
        final String expectedName = "Series";
        final String expectedDescription = "A categoria mais assistida ";
        final boolean expectedIsActive = false;

        //when
        final Category aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        //then
        Assertions.assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));
        Assertions.assertNotNull(aCategory);
        Assertions.assertNotNull(aCategory.getId());
        Assertions.assertNotNull(aCategory.getCreatedAt());
        Assertions.assertNotNull(aCategory.getUpdatedAt());
        Assertions.assertNotNull(aCategory.getDeletedAt());
        Assertions.assertEquals(aCategory.getName(), expectedName);
        Assertions.assertEquals(aCategory.getDescription(), expectedDescription);
        Assertions.assertEquals(aCategory.isActive(), expectedIsActive);
    }

    @Test
    void givenAValidActiveCategory_whenCallDeactivate_thenReturnCategoryInactivated(){
        //given
        final String expectedName = "Series";
        final String expectedDescription = "A categoria mais assistida ";
        final boolean expectedIsActive = false;

        //first when
        final Category aCategory = Category.newCategory(expectedName, expectedDescription, true);
         Assertions.assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));
        final Instant updatedAt = aCategory.getUpdatedAt();

        //first then
        Assertions.assertTrue(aCategory.isActive());
        Assertions.assertNull(aCategory.getDeletedAt());

        //second when
        final Category atualCategory = aCategory.deactivate();

        //second then
        Assertions.assertDoesNotThrow(() -> atualCategory.validate(new ThrowsValidationHandler()));
        Assertions.assertEquals(atualCategory.getId(), aCategory.getId());
        Assertions.assertEquals(atualCategory.getCreatedAt(), aCategory.getCreatedAt());
//        Assertions.assertTrue(atualCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNotNull(atualCategory.getDeletedAt());
        Assertions.assertEquals(atualCategory.getName(), expectedName);
        Assertions.assertEquals(atualCategory.getDescription(), expectedDescription);
        Assertions.assertEquals(atualCategory.isActive(), expectedIsActive);
    }

    @Test
    void givenAnInvalidInCategory_whenCallActivate_thenReturnCategoryActivated(){
        //given
        final String expectedName = "Series";
        final String expectedDescription = "A categoria mais assistida ";
        final boolean expectedIsActive = true;

        //first when
        final Category aCategory = Category.newCategory(expectedName, expectedDescription, false);
        Assertions.assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));
        final Instant updatedAt = aCategory.getUpdatedAt();

        //first then
        Assertions.assertFalse(aCategory.isActive());
        Assertions.assertNotNull(aCategory.getDeletedAt());

        //second when
        final Category atualCategory = aCategory.activate();

        //second then
        Assertions.assertDoesNotThrow(() -> atualCategory.validate(new ThrowsValidationHandler()));
        Assertions.assertEquals(atualCategory.getId(), aCategory.getId());
        Assertions.assertEquals(atualCategory.getCreatedAt(), aCategory.getCreatedAt());
//        Assertions.assertTrue(atualCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(atualCategory.getDeletedAt());
        Assertions.assertEquals(atualCategory.getName(), expectedName);
        Assertions.assertEquals(atualCategory.getDescription(), expectedDescription);
        Assertions.assertEquals(atualCategory.isActive(), expectedIsActive);
    }

    @Test
    void givenAValidCategory_whenCallUpdate_thenReturnCategoryUpdated(){
        //given
        final String expectedName = "Series";
        final String expectedDescription = "A categoria mais assistida ";
        final boolean expectedIsActive = true;

        //first when
        final Category aCategory = Category.newCategory("Filmes", "Categoria", expectedIsActive);
        Assertions.assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));

        final Instant createdAt = aCategory.getCreatedAt();
        final Instant updatedAt = aCategory.getUpdatedAt();

        //second when
        final Category atualCategory = aCategory.update(expectedName, expectedDescription, expectedIsActive);

        //second then
        Assertions.assertDoesNotThrow(() -> atualCategory.validate(new ThrowsValidationHandler()));
        Assertions.assertEquals(atualCategory.getId(), aCategory.getId());
        Assertions.assertEquals(atualCategory.getCreatedAt(), createdAt);
//        Assertions.assertTrue(atualCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(atualCategory.getDeletedAt());
        Assertions.assertEquals(atualCategory.getName(), expectedName);
        Assertions.assertEquals(atualCategory.getDescription(), expectedDescription);
        Assertions.assertEquals(atualCategory.isActive(), expectedIsActive);
    }

    @Test
    void givenAValidCategory_whenCallUpdateToInactive_thenReturnCategoryUpdated(){
        //given
        final String expectedName = "Series";
        final String expectedDescription = "A categoria mais assistida ";
        final boolean expectedIsActive = false;

        //first when
        final Category aCategory = Category.newCategory("Filmes", "Categoria", true);
        Assertions.assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));

        final Instant createdAt = aCategory.getCreatedAt();
        final Instant updatedAt = aCategory.getUpdatedAt();

        //first then
        Assertions.assertTrue(aCategory.isActive());
        Assertions.assertNull(aCategory.getDeletedAt());

        //second when
        final Category atualCategory = aCategory.update(expectedName, expectedDescription, expectedIsActive);

        //second then
        Assertions.assertDoesNotThrow(() -> atualCategory.validate(new ThrowsValidationHandler()));
        Assertions.assertEquals(atualCategory.getId(), aCategory.getId());
        Assertions.assertEquals(atualCategory.getCreatedAt(), createdAt);
//        Assertions.assertTrue(atualCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNotNull(atualCategory.getDeletedAt());
        Assertions.assertEquals(atualCategory.getName(), expectedName);
        Assertions.assertEquals(atualCategory.getDescription(), expectedDescription);
        Assertions.assertEquals(atualCategory.isActive(), expectedIsActive);
    }

    @Test
    void givenAValidCategory_whenCallUpdateWithInvalidParams_thenReturnCategoryUpdated(){
        //given
        final String expectedName = null;
        final String expectedDescription = "A categoria mais assistida ";
        final boolean expectedIsActive = true;

        //first when
        final Category aCategory = Category.newCategory("Filmes", "Categoria", expectedIsActive);
        Assertions.assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));

        final Instant createdAt = aCategory.getCreatedAt();
        final Instant updatedAt = aCategory.getUpdatedAt();


        //second when
        final Category atualCategory = aCategory.update(expectedName, expectedDescription, expectedIsActive);

        //second then
        Assertions.assertEquals(atualCategory.getId(), aCategory.getId());
        Assertions.assertEquals(atualCategory.getCreatedAt(), createdAt);
//        Assertions.assertTrue(atualCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(atualCategory.getDeletedAt());
        Assertions.assertEquals(atualCategory.getName(), expectedName);
        Assertions.assertEquals(atualCategory.getDescription(), expectedDescription);
        Assertions.assertEquals(atualCategory.isActive(), expectedIsActive);
    }
}