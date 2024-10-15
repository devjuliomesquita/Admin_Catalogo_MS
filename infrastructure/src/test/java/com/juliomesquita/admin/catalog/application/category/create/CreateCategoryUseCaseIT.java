package com.juliomesquita.admin.catalog.application.category.create;

import com.juliomesquita.admin.catalog.IntegrationTest;
import com.juliomesquita.admin.catalog.application.usecases.category.create.CreateCategoryCommand;
import com.juliomesquita.admin.catalog.application.usecases.category.create.CreateCategoryOutput;
import com.juliomesquita.admin.catalog.application.usecases.category.create.CreateCategoryUseCase;
import com.juliomesquita.admin.catalog.domain.category.Category;
import com.juliomesquita.admin.catalog.domain.category.CategoryGateway;
import com.juliomesquita.admin.catalog.domain.category.CategoryId;
import com.juliomesquita.admin.catalog.domain.commom.validation.Notification;
import com.juliomesquita.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@IntegrationTest
public class CreateCategoryUseCaseIT {
    @Autowired
    private CreateCategoryUseCase useCase;

    @Autowired
    @SpyBean
    private CategoryGateway categoryGateway;

    @Autowired
    private CategoryRepository categoryRepository;


    @Test
    void givenAValidCommand_whenCallsCreateCategory_thenReturnCategoryId() {
        //verify database
        assertEquals(0, this.categoryRepository.count());

        //given
        final String expectedName = "Terror";
        final String expectedDescription = "Categoria mais assistida.";
        final boolean expectedIsActive = true;

        final CreateCategoryCommand aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        //when
        final CreateCategoryOutput actualOutput = this.useCase.execute(aCommand).get();

        //then
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());
        assertEquals(1, this.categoryRepository.count());

        final Category categoryRetrieved = this.categoryGateway.findById(CategoryId.from(actualOutput.id())).get();

        assertAll("Verify attributes category", () -> {
            assertEquals(actualOutput.id(), categoryRetrieved.getId().getValue());
            assertEquals(expectedName, categoryRetrieved.getName());
            assertEquals(expectedDescription, categoryRetrieved.getDescription());
            assertEquals(expectedIsActive, categoryRetrieved.isActive());
            assertNotNull(categoryRetrieved.getCreatedAt());
            assertNotNull(categoryRetrieved.getUpdatedAt());
            assertNull(categoryRetrieved.getDeletedAt());
        });
    }

    @Test
    void givenAInvalidNameParams_whenCallsCreateCategory_thenReturnDomainException() {
        //verify database
        assertEquals(0, this.categoryRepository.count());

        //given
        final String expectedName = null;
        final String expectedDescription = "Categoria mais assistida.";
        final boolean expectedIsActive = true;
        final String expectedMessageError = "'name' should be not null";
        final int expectedMessageErrorCount = 1;

        final CreateCategoryCommand aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        //when
        final Notification notification = this.useCase.execute(aCommand).getLeft();

        //then
        assertEquals(expectedMessageError, notification.firstError().message());
        assertEquals(expectedMessageErrorCount, notification.getErrors().size());

        //verify database
        assertEquals(0, this.categoryRepository.count());

        verify(this.categoryGateway, times(0)).create(any());
    }

    @Test
    void givenAValidCommandWithInactiveCategory_whenCallsCreateCategory_thenReturnInactiveCategoryId() {
        //verify database
        assertEquals(0, this.categoryRepository.count());

        //given
        final String expectedName = "Terror";
        final String expectedDescription = "Categoria mais assistida.";
        final boolean expectedIsActive = false;

        final CreateCategoryCommand aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        //when
        final CreateCategoryOutput actualOutput = this.useCase.execute(aCommand).get();

        //then
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        //verify database
        assertEquals(1, this.categoryRepository.count());

        final Category categoryRetrieved = this.categoryGateway.findById(CategoryId.from(actualOutput.id())).get();

        assertAll("Verify attributes category", () -> {
            assertEquals(actualOutput.id(), categoryRetrieved.getId().getValue());
            assertEquals(expectedName, categoryRetrieved.getName());
            assertEquals(expectedDescription, categoryRetrieved.getDescription());
            assertFalse(categoryRetrieved.isActive());
            assertNotNull(categoryRetrieved.getCreatedAt());
            assertNotNull(categoryRetrieved.getUpdatedAt());
            assertNotNull(categoryRetrieved.getDeletedAt());
        });

    }

    @Test
    void givenAValidCommand_whenGatewayThrowsRandomException_thenReturnAnException() {
        //verify database
        assertEquals(0, this.categoryRepository.count());

        //given
        final String expectedName = "Terror";
        final String expectedDescription = "Categoria mais assistida.";
        final boolean expectedIsActive = true;
        final String expectedMessageError = "Gateway Error";
        final int expectedMessageErrorCount = 1;

        final CreateCategoryCommand aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        //when
        doThrow(new IllegalArgumentException(expectedMessageError)).when(this.categoryGateway).create(any());
        final Notification notification = this.useCase.execute(aCommand).getLeft();

        //then
        assertEquals(expectedMessageError, notification.firstError().message());
        assertEquals(expectedMessageErrorCount, notification.getErrors().size());
    }
}
