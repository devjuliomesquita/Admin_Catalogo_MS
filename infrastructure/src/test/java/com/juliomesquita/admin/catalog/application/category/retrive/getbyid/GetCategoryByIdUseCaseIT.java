package com.juliomesquita.admin.catalog.application.category.retrive.getbyid;

import com.juliomesquita.admin.catalog.IntegrationTest;
import com.juliomesquita.admin.catalog.application.usecases.category.retrive.getbyid.CategoryOutput;
import com.juliomesquita.admin.catalog.application.usecases.category.retrive.getbyid.GetCategoryByIdUseCase;
import com.juliomesquita.admin.catalog.domain.category.Category;
import com.juliomesquita.admin.catalog.domain.category.CategoryGateway;
import com.juliomesquita.admin.catalog.domain.category.CategoryId;
import com.juliomesquita.admin.catalog.domain.commom.exceptions.DomainException;
import com.juliomesquita.admin.catalog.domain.commom.exceptions.NotFoundException;
import com.juliomesquita.admin.catalog.infrastructure.category.persistence.CategoryEntity;
import com.juliomesquita.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@IntegrationTest
public class GetCategoryByIdUseCaseIT {

    @Autowired
    private GetCategoryByIdUseCase useCase;

    @Autowired
    @SpyBean
    private CategoryGateway categoryGateway;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void givenAValidParamId_whenCallsGetByIdCategory_thenReturnCategory() {
        //verify database
        assertEquals(0, this.categoryRepository.count());

        //given
        final String expectedName = "Terror";
        final String expectedDescription = "Categoria mais assistida.";
        final boolean expectedIsActive = true;
        final Category aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final CategoryId expectedId = aCategory.getId();
        this.categoryRepository.saveAndFlush(CategoryEntity.from(aCategory));

        //verify database
        assertEquals(1, this.categoryRepository.count());

        //when
        final CategoryOutput response = this.useCase.execute(expectedId.getValue());

        //then
        assertAll("Verify attributes category", () -> {
            assertEquals(aCategory.getId().getValue(), response.id());
            assertEquals(aCategory.getName(), response.name());
            assertEquals(aCategory.getDescription(), response.description());
            assertEquals(aCategory.isActive(), response.active());
            assertNotNull(response.createdAt());
            assertNotNull(response.updatedAt());
            assertNull(response.deletedAt());
        });
        verify(this.categoryGateway, times(1)).findById(eq(expectedId));
    }

    @Test
    void givenAInvalidParamId_whenCallsGetByIdCategory_thenReturnNotFound() {
        //verify database
        assertEquals(0, this.categoryRepository.count());

        //given
        final CategoryId expectedId = CategoryId.from("96cf5faa-5921-449a-b3a6-dae04cf7cdea");
        final String expectedMessageError = "Category with ID 96cf5faa-5921-449a-b3a6-dae04cf7cdea was not found";
        final int expectedMessageErrorCount = 0;

        //then
        final DomainException domainException = assertThrows(NotFoundException.class, () -> this.useCase.execute(expectedId.getValue()));
        assertEquals(expectedMessageError, domainException.getMessage());
        assertEquals(expectedMessageErrorCount, domainException.getErrors().size());
        verify(this.categoryGateway, times(1)).findById(eq(expectedId));
    }

    @Test
    void givenAValidParamId_whenCallsGetByIdCategory_thenReturnException() {
        //verify database
        assertEquals(0, this.categoryRepository.count());

        //given
        final CategoryId expectedId = CategoryId.from(UUID.randomUUID());
        final String expectedMessageError = "Gateway error";

        //when
        doThrow(new IllegalArgumentException(expectedMessageError)).when(this.categoryGateway).findById(expectedId);

        //then
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> this.useCase.execute(expectedId.getValue()));
        assertEquals(expectedMessageError, exception.getMessage());
        verify(this.categoryGateway, times(1)).findById(eq(expectedId));
    }
}
