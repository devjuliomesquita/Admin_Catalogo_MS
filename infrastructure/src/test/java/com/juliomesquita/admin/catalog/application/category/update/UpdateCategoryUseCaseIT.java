package com.juliomesquita.admin.catalog.application.category.update;

import com.juliomesquita.admin.catalog.IntegrationTest;
import com.juliomesquita.admin.catalog.application.usecases.category.update.UpdateCategoryCommand;
import com.juliomesquita.admin.catalog.application.usecases.category.update.UpdateCategoryOutput;
import com.juliomesquita.admin.catalog.application.usecases.category.update.UpdateCategoryUseCase;
import com.juliomesquita.admin.catalog.domain.category.Category;
import com.juliomesquita.admin.catalog.domain.category.CategoryGateway;
import com.juliomesquita.admin.catalog.domain.category.CategoryId;
import com.juliomesquita.admin.catalog.domain.commom.exceptions.DomainException;
import com.juliomesquita.admin.catalog.domain.commom.exceptions.NotFoundException;
import com.juliomesquita.admin.catalog.domain.commom.validation.Notification;
import com.juliomesquita.admin.catalog.infrastructure.category.persistence.CategoryEntity;
import com.juliomesquita.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@IntegrationTest
public class UpdateCategoryUseCaseIT {
    @Autowired
    private UpdateCategoryUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    void givenAValidParams_whenCallsUpdateCategory_thenReturnCategoryId() {
        //verify database
        assertEquals(0, this.categoryRepository.count());

        //given
        final Category aCategory = Category.newCategory("Cat", "Qualquer", true);
        this.categoryRepository.saveAndFlush(CategoryEntity.from(aCategory));

        final String expectedName = "Terror";
        final String expectedDescription = "Categoria mais assistida.";
        final boolean expectedIsActive = true;
        final CategoryId expectedId = aCategory.getId();

        final UpdateCategoryCommand aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        //when
        final UpdateCategoryOutput response = this.useCase.execute(aCommand).get();

        //then
        assertNotNull(response);
        assertNotNull(response.id());

        verify(this.categoryGateway, times(1)).findById(Mockito.eq(expectedId));
        verify(this.categoryGateway, times(1)).update(any());

        final Category categoryRetrieved = this.categoryGateway.findById(CategoryId.from(response.id())).get();

        assertAll("Verify attributes category", () -> {
            assertEquals(response.id(), categoryRetrieved.getId().getValue());
            assertEquals(expectedName, categoryRetrieved.getName());
            assertEquals(expectedDescription, categoryRetrieved.getDescription());
            assertEquals(expectedIsActive, categoryRetrieved.isActive());
            assertNotNull(categoryRetrieved.getCreatedAt());
            assertTrue(categoryRetrieved.getUpdatedAt().isAfter(aCategory.getUpdatedAt()));
            assertNull(categoryRetrieved.getDeletedAt());
        });
    }

    @Test
    void givenAInvalidNameParams_whenCallsUpdateCategory_thenReturnDomainException() {
        //verify database
        assertEquals(0, this.categoryRepository.count());

        //given
        final Category aCategory = Category.newCategory("Suspense", "Qualquer descrição", true);
        this.categoryRepository.saveAndFlush(CategoryEntity.from(aCategory));

        final String expectedName = null;
        final String expectedDescription = "Categoria mais assistida.";
        final boolean expectedIsActive = true;
        final String expectedMessageError = "'name' should be not null";
        final int expectedMessageErrorCount = 1;

        final UpdateCategoryCommand aCommand = UpdateCategoryCommand.with(
                aCategory.getId().getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        //verify database
        assertEquals(1, this.categoryRepository.count());

        //when
        final Notification notification = this.useCase.execute(aCommand).getLeft();

        //then
        assertEquals(expectedMessageError, notification.firstError().message());
        assertEquals(expectedMessageErrorCount, notification.getErrors().size());
        verify(this.categoryGateway, times(1)).findById(eq(aCategory.getId()));
        verify(this.categoryGateway, times(0)).update(any());

        final Category categoryRetrieved = this.categoryGateway.findById(aCategory.getId()).get();

        assertAll("Verify attributes category", () -> {
            assertEquals(aCategory.getId(), categoryRetrieved.getId());
            assertEquals(aCategory.getName(), categoryRetrieved.getName());
            assertEquals(aCategory.getDescription(), categoryRetrieved.getDescription());
            assertEquals(aCategory.isActive(), categoryRetrieved.isActive());
            assertNotNull(categoryRetrieved.getCreatedAt());
            assertNotNull(categoryRetrieved.getUpdatedAt());
            assertNull(categoryRetrieved.getDeletedAt());
        });

        //verify database
        assertEquals(1, this.categoryRepository.count());
    }

    @Test
    void givenAValidCommandWithInactiveCategory_whenCallsUpdateCategory_thenReturnCategoryInactivated() {
        //verify database
        assertEquals(0, this.categoryRepository.count());

        //given
        final Category aCategory = Category.newCategory("Cat", "Qualquer", true);
        this.categoryRepository.saveAndFlush(CategoryEntity.from(aCategory));

        final String expectedName = "Terror";
        final String expectedDescription = "Categoria mais assistida.";
        final boolean expectedIsActive = false;
        final CategoryId expectedId = aCategory.getId();

        //verify database
        assertEquals(1, this.categoryRepository.count());

        final UpdateCategoryCommand aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        //first then
        assertTrue(aCategory.isActive());
        assertNull(aCategory.getDeletedAt());

        //when
        final UpdateCategoryOutput response = this.useCase.execute(aCommand).get();

        //second then
        assertNotNull(response);
        assertNotNull(response.id());

        verify(this.categoryGateway, times(1)).findById(Mockito.eq(expectedId));
        verify(this.categoryGateway, times(1)).update(any());

        final Category categoryRetrieved = this.categoryGateway.findById(CategoryId.from(response.id())).get();

        assertAll("Verify attributes category", () -> {
            assertEquals(aCategory.getId(), categoryRetrieved.getId());
            assertEquals(expectedName, categoryRetrieved.getName());
            assertEquals(expectedDescription, categoryRetrieved.getDescription());
            assertFalse(categoryRetrieved.isActive());
            assertNotNull(categoryRetrieved.getCreatedAt());
            assertTrue(categoryRetrieved.getUpdatedAt().isAfter(aCategory.getUpdatedAt()));
            assertNotNull(categoryRetrieved.getDeletedAt());
        });

        //verify database
        assertEquals(1, this.categoryRepository.count());
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

        final Category aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        this.categoryRepository.saveAndFlush(CategoryEntity.from(aCategory));

        //verify database
        assertEquals(1, this.categoryRepository.count());

        final UpdateCategoryCommand aCommand = UpdateCategoryCommand.with(
                aCategory.getId().getValue(),
                expectedName,
                "Categoria menos assistida.",
                expectedIsActive
        );

        //when
        doThrow(new IllegalArgumentException(expectedMessageError)).when(this.categoryGateway).update(any());
        final Notification notification = this.useCase.execute(aCommand).getLeft();

        //then
        assertEquals(expectedMessageError, notification.firstError().message());
        assertEquals(expectedMessageErrorCount, notification.getErrors().size());

        verify(this.categoryGateway, times(1)).findById(eq(aCategory.getId()));
        verify(this.categoryGateway, times(1)).update(any());

        final Category categoryRetrieved = this.categoryGateway.findById(aCategory.getId()).get();

        assertAll("Verify attributes category", () -> {
            assertEquals(aCategory.getId(), categoryRetrieved.getId());
            assertEquals(expectedName, categoryRetrieved.getName());
            assertEquals(expectedDescription, categoryRetrieved.getDescription());
            assertEquals(expectedIsActive, categoryRetrieved.isActive());
            assertNotNull(categoryRetrieved.getCreatedAt());
            assertNotNull(categoryRetrieved.getUpdatedAt());
            assertNull(categoryRetrieved.getDeletedAt());
        });

        //verify database
        assertEquals(1, this.categoryRepository.count());
    }

    @Test
    void givenACommandWithInvalidId_whenCallsUpdateCategory_thenReturnNotFoundException() {
        //verify database
        assertEquals(0, this.categoryRepository.count());

        //given
        final String expectedName = "Terror";
        final String expectedDescription = "Categoria mais assistida.";
        final boolean expectedIsActive = true;
        final UUID uuidMock = UUID.randomUUID();
        final String expectedMessageError = "Category with ID %s was not found".formatted(uuidMock);
        final int expectedMessageErrorCount = 0;

        final UpdateCategoryCommand aCommand = UpdateCategoryCommand.with(
                uuidMock.toString(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );


        //then
        final NotFoundException domainException = assertThrows(NotFoundException.class, () -> this.useCase.execute(aCommand));
        assertEquals(expectedMessageError, domainException.getMessage());
        assertEquals(expectedMessageErrorCount, domainException.getErrors().size());

        verify(this.categoryGateway, times(1)).findById(eq(CategoryId.from(uuidMock)));
        verify(this.categoryGateway, times(0)).update(any());
    }
}
