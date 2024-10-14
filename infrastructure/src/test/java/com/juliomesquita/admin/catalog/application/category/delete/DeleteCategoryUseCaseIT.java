package com.juliomesquita.admin.catalog.application.category.delete;

import com.juliomesquita.admin.catalog.IntegrationTest;
import com.juliomesquita.admin.catalog.application.usecases.category.delete.DeleteCategoryUseCase;
import com.juliomesquita.admin.catalog.domain.category.Category;
import com.juliomesquita.admin.catalog.domain.category.CategoryGateway;
import com.juliomesquita.admin.catalog.domain.category.CategoryId;
import com.juliomesquita.admin.catalog.infrastructure.category.persistence.CategoryEntity;
import com.juliomesquita.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@IntegrationTest
public class DeleteCategoryUseCaseIT {
    @Autowired
    private DeleteCategoryUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    void givenAValidParamId_whenCallsDeleteCategory_thenReturnVoid() {
        //verify database
        assertEquals(0, this.categoryRepository.count());

        //given
        final Category aCategory = Category.newCategory("Suspence", "Series mais assistidas", true);
        final CategoryId expectedId = aCategory.getId();
        this.categoryRepository.saveAndFlush(CategoryEntity.from(aCategory));

        //verify database
        assertEquals(1, this.categoryRepository.count());

        //then
        assertDoesNotThrow(() -> this.useCase.execute(expectedId.getValue()));
        verify(this.categoryGateway, times(1)).deleteById(eq(expectedId));

        //verify database
        assertEquals(0, this.categoryRepository.count());
    }

    @Test
    void givenAInvalidParamId_whenCallsDeleteCategory_thenReturnVoid() {
        //verify database
        assertEquals(0, this.categoryRepository.count());

        //given
        final CategoryId expectedId = CategoryId.from(UUID.randomUUID());

        //then
        assertDoesNotThrow(() -> this.useCase.execute(expectedId.getValue()));
        verify(this.categoryGateway, times(1)).deleteById(eq(expectedId));
    }

    @Test
    void givenAValidParamId_whenCallsDeleteCategory_thenReturnAnyException() {
        //verify database
        assertEquals(0, this.categoryRepository.count());

        //given
        final Category aCategory = Category.newCategory("Suspence", "Series mais assistidas", true);
        final CategoryId expectedId = aCategory.getId();
        final String expectedMessageError = "Gateway Error";

        //when
        doThrow(new IllegalArgumentException(expectedMessageError)).when(this.categoryGateway).deleteById(expectedId);

        //then
        String messageError = assertThrows(IllegalArgumentException.class, () -> this.useCase.execute(expectedId.getValue())).getMessage();
        assertEquals(expectedMessageError, messageError);
        verify(this.categoryGateway, times(1)).deleteById(eq(expectedId));
    }
}
