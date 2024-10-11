package com.juliomesquita.admin.catalog.application.usecases.category.update;

import com.juliomesquita.admin.catalog.application.usecases.category.create.CreateCategoryCommand;
import com.juliomesquita.admin.catalog.domain.category.Category;
import com.juliomesquita.admin.catalog.domain.category.CategoryGateway;
import com.juliomesquita.admin.catalog.domain.category.CategoryId;
import com.juliomesquita.admin.catalog.domain.commom.exceptions.DomainException;
import com.juliomesquita.admin.catalog.domain.commom.validation.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultUpdateCategoryUseCaseTest {
    @InjectMocks
    private DefaultUpdateCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp(){
        Mockito.reset(categoryGateway);
    }

    @Test
    void givenAValidParams_whenCallsUpdateCategory_thenReturnCategoryId() {
        //given
        final Category aCategory = Category.newCategory("Cat", "Qualquer", true);
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
        when(this.categoryGateway.findById(Mockito.eq(expectedId))).thenReturn(Optional.of(aCategory.clone()));
        when(this.categoryGateway.update(any())).thenAnswer(returnsFirstArg());
        final UpdateCategoryOutput response = this.useCase.execute(aCommand).get();

        //then
        assertNotNull(response);
        assertNotNull(response.id());

        verify(this.categoryGateway, times(1)).findById(Mockito.eq(expectedId));
        verify(this.categoryGateway, times(1))
                .update(argThat(category -> {
                    return Objects.equals(expectedName, category.getName())
                            && Objects.equals(expectedDescription, category.getDescription())
                            && Objects.equals(expectedIsActive, category.isActive())
                            && Objects.equals(expectedId, category.getId())
                            && Objects.equals(aCategory.getCreatedAt(), category.getCreatedAt())
                            && aCategory.getUpdatedAt().isBefore(category.getUpdatedAt())
                            && Objects.isNull(category.getDeletedAt());
                }));
    }

    @Test
    void givenAInvalidNameParams_whenCallsUpdateCategory_thenReturnDomainException() {
        //given
        final Category aCategory = Category.newCategory("Suspense", "Qualquer descrição", true);
        final String expectedName = null;
        final String expectedDescription = "Categoria mais assistida.";
        final boolean expectedIsActive = true;
        final String expectedMessageError = "'name' should be not null";
        final int expectedMessageErrorCount = 1;

        final UpdateCategoryCommand aCommand = UpdateCategoryCommand.with(
                aCategory.getId().getValue()
                , expectedName,
                expectedDescription,
                expectedIsActive
        );

        //when
        when(this.categoryGateway.findById(eq(aCategory.getId()))).thenReturn(Optional.of(aCategory.clone()));
        final Notification notification = this.useCase.execute(aCommand).getLeft();

        //then
        assertEquals(expectedMessageError, notification.firstError().message());
        assertEquals(expectedMessageErrorCount, notification.getErrors().size());
        verify(this.categoryGateway, times(1)).findById(eq(aCategory.getId()));
        verify(this.categoryGateway, times(0)).update(any());
    }

    @Test
    void givenAValidCommandWithInactiveCategory_whenCallsUpdateCategory_thenReturnCategoryInactivated() {
        //given
        final Category aCategory = Category.newCategory("Cat", "Qualquer", true);
        final String expectedName = "Terror";
        final String expectedDescription = "Categoria mais assistida.";
        final boolean expectedIsActive = false;
        final CategoryId expectedId = aCategory.getId();

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
        when(this.categoryGateway.findById(Mockito.eq(expectedId))).thenReturn(Optional.of(aCategory.clone()));
        when(this.categoryGateway.update(any())).thenAnswer(returnsFirstArg());
        final UpdateCategoryOutput response = this.useCase.execute(aCommand).get();

        //second then
        assertNotNull(response);
        assertNotNull(response.id());

        verify(this.categoryGateway, times(1)).findById(Mockito.eq(expectedId));
        verify(this.categoryGateway, times(1))
                .update(argThat(category -> {
                    return Objects.equals(expectedName, category.getName())
                            && Objects.equals(expectedDescription, category.getDescription())
                            && Objects.equals(expectedIsActive, category.isActive())
                            && Objects.equals(expectedId, category.getId())
                            && Objects.equals(aCategory.getCreatedAt(), category.getCreatedAt())
                            && aCategory.getUpdatedAt().isBefore(category.getUpdatedAt())
                            && Objects.nonNull(category.getDeletedAt());
                }));
    }

    @Test
    void givenAValidCommand_whenGatewayThrowsRandomException_thenReturnAnException() {
        //given
        final Category aCategory = Category.newCategory("Cat", "Qualquer", true);
        final String expectedName = "Terror";
        final String expectedDescription = "Categoria mais assistida.";
        final boolean expectedIsActive = true;
        final String expectedMessageError = "Gateway Error";
        final int expectedMessageErrorCount = 1;

        final UpdateCategoryCommand aCommand = UpdateCategoryCommand.with(
                aCategory.getId().getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        //when
        when(this.categoryGateway.findById(Mockito.eq(aCategory.getId()))).thenReturn(Optional.of(aCategory.clone()));
        when(this.categoryGateway.update(any())).thenThrow(new IllegalArgumentException(expectedMessageError));
        final Notification notification = this.useCase.execute(aCommand).getLeft();

        //then
        assertEquals(expectedMessageError, notification.firstError().message());
        assertEquals(expectedMessageErrorCount, notification.getErrors().size());

        verify(this.categoryGateway, times(1)).findById(eq(aCategory.getId()));
        verify(this.categoryGateway, times(1))
                .update(argThat(categoryUpdated -> {
                    return Objects.equals(expectedName, categoryUpdated.getName())
                            && Objects.equals(expectedDescription, categoryUpdated.getDescription())
                            && Objects.equals(expectedIsActive, categoryUpdated.isActive())
                            && Objects.nonNull(categoryUpdated.getCreatedAt())
                            && Objects.nonNull(categoryUpdated.getUpdatedAt())
                            && Objects.isNull(categoryUpdated.getDeletedAt());
                }));
    }

    @Test
    void givenACommandWithInvalidId_whenCallsUpdateCategory_thenReturnNotFoundException() {
        //given
        final String expectedName = "Terror";
        final String expectedDescription = "Categoria mais assistida.";
        final boolean expectedIsActive = true;
        final UUID uuidMock = UUID.randomUUID();
        final String expectedMessageError = "Category with ID %s was not found".formatted(uuidMock);
        final int expectedMessageErrorCount = 1;

        final UpdateCategoryCommand aCommand = UpdateCategoryCommand.with(
                uuidMock.toString(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        //when
        when(this.categoryGateway.findById(eq(CategoryId.from(uuidMock)))).thenReturn(Optional.empty());

        //then
        final DomainException domainException = assertThrows(DomainException.class, () -> this.useCase.execute(aCommand));
        assertEquals(expectedMessageError, domainException.getErrors().get(0).message());
        assertEquals(expectedMessageErrorCount, domainException.getErrors().size());

        verify(this.categoryGateway, times(1)).findById(eq(CategoryId.from(uuidMock)));
        verify(this.categoryGateway, times(0)).update(any());
    }
}