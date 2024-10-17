package com.juliomesquita.admin.catalog.application.usecases.category.retrive.getbyid;

import com.juliomesquita.admin.catalog.domain.category.Category;
import com.juliomesquita.admin.catalog.domain.category.CategoryGateway;
import com.juliomesquita.admin.catalog.domain.category.CategoryId;
import com.juliomesquita.admin.catalog.domain.commom.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultGetCategoryByIdUseCaseTest {
    @InjectMocks
    private DefaultGetCategoryByIdUseCase useCase;
    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(categoryGateway);
    }

    @Test
    void givenAValidParamId_whenCallsGetByIdCategory_thenReturnCategory() {
        //given
        final String expectedName = "Terror";
        final String expectedDescription = "Categoria mais assistida.";
        final boolean expectedIsActive = true;
        final Category aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final CategoryId expectedId = aCategory.getId();

        //when
        when(this.categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(aCategory.clone()));
        final CategoryOutput response = this.useCase.execute(expectedId.getValue());

        //then
        assertEquals(CategoryOutput.from(aCategory), response);
        assertAll("Verify attributes category", () -> {
            assertEquals(aCategory.getId().getValue(), response.id());
            assertEquals(aCategory.getName(), response.name());
            assertEquals(aCategory.getDescription(), response.description());
            assertEquals(aCategory.isActive(), response.active());
            assertEquals(aCategory.getCreatedAt(), response.createdAt());
            assertEquals(aCategory.getUpdatedAt(), response.updatedAt());
            assertEquals(aCategory.getDeletedAt(), response.deletedAt());
        });
        verify(this.categoryGateway, times(1)).findById(eq(expectedId));
    }

    @Test
    void givenAInvalidParamId_whenCallsGetByIdCategory_thenReturnNotFound() {
        //given
        final CategoryId expectedId = CategoryId.from("6a3501da-cf04-49db-9a30-34a5ecf60119");
        final String expectedMessageError = "Category with ID 6a3501da-cf04-49db-9a30-34a5ecf60119 was not found";
        final int expectedMessageErrorCount = 0;

        //when
        when(this.categoryGateway.findById(eq(expectedId))).thenReturn(Optional.empty());

        //then
        final NotFoundException domainException = assertThrows(NotFoundException.class, () -> this.useCase.execute(expectedId.getValue()));
        assertEquals(expectedMessageError, domainException.getMessage());
        assertEquals(expectedMessageErrorCount, domainException.getErrors().size());
        verify(this.categoryGateway, times(1)).findById(eq(expectedId));
    }

    @Test
    void givenAValidParamId_whenCallsGetByIdCategory_thenReturnException() {
        //given
        final CategoryId expectedId = CategoryId.from(UUID.randomUUID());
        final String expectedMessageError = "Gateway error";

        //when
        when(this.categoryGateway.findById(eq(expectedId)))
                .thenThrow(new IllegalArgumentException(expectedMessageError));

        //then
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> this.useCase.execute(expectedId.getValue()));
        assertEquals(expectedMessageError, exception.getMessage());
        verify(this.categoryGateway, times(1)).findById(eq(expectedId));
    }
}