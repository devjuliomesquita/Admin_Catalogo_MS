package com.juliomesquita.admin.catalog.application.usecases.category.delete;

import com.juliomesquita.admin.catalog.domain.category.Category;
import com.juliomesquita.admin.catalog.domain.category.CategoryGateway;
import com.juliomesquita.admin.catalog.domain.category.CategoryId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultDeleteCategoryUseCaseTest {

    @InjectMocks
    private DefaultDeleteCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(categoryGateway);
    }

    @Test
    void givenAValidParamId_whenCallsDeleteCategory_thenReturnVoid() {
        //given
        final Category aCategory = Category.newCategory("Suspence", "Series mais assistidas", true);
        final CategoryId expectedId = aCategory.getId();

        //when
        doNothing().when(this.categoryGateway).deleteById(eq(expectedId));

        //then
        assertDoesNotThrow(() -> this.useCase.execute(expectedId.getValue()));
        verify(this.categoryGateway, times(1)).deleteById(eq(expectedId));
    }

    @Test
    void givenAInvalidParamId_whenCallsDeleteCategory_thenReturnVoid() {
        //given
        final CategoryId expectedId = CategoryId.from(UUID.randomUUID());

        //when
        doNothing().when(this.categoryGateway).deleteById(eq(expectedId));

        //then
        assertDoesNotThrow(() -> this.useCase.execute(expectedId.getValue()));
        verify(this.categoryGateway, times(1)).deleteById(eq(expectedId));
    }

    @Test
    void givenAValidParamId_whenCallsDeleteCategory_thenReturnAnyException() {
        //given
        final Category aCategory = Category.newCategory("Suspence", "Series mais assistidas", true);
        final CategoryId expectedId = aCategory.getId();
        final String expectedMessageError = "Gateway Error";

        //when
        doThrow(new IllegalArgumentException(expectedMessageError)).when(this.categoryGateway).deleteById(eq(expectedId));

        //then
        String messageError = assertThrows(IllegalArgumentException.class, () -> this.useCase.execute(expectedId.getValue())).getMessage();
        assertEquals(expectedMessageError, messageError);
        verify(this.categoryGateway, times(1)).deleteById(eq(expectedId));
    }

}