package com.juliomesquita.admin.catalog.application.usecases.genre.create;

import com.juliomesquita.admin.catalog.domain.category.CategoryGateway;
import com.juliomesquita.admin.catalog.domain.category.CategoryId;
import com.juliomesquita.admin.catalog.domain.genre.GenreGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@DisplayName("Create Genre - Use Case - Application")
@ExtendWith(MockitoExtension.class)
class DefaultCreateGenreUseCaseTest {
    @InjectMocks
    private DefaultCreateGenreUseCase useCase;
    @Mock
    private GenreGateway genreGateway;
    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        reset(genreGateway, categoryGateway);
    }

    @Test
    void givenAValidCommand_whenCallsCreateGenre_thenReturnGenreId() {
        //given
        final String expectedName = "Animes";
        final Boolean expectedIsActive = true;
//        final List<String> categories = List.of(UUID.randomUUID().toString());
        final List<String> categories = List.of();
        final CreateGenreCommand aCommand =
                CreateGenreCommand.with(expectedName, expectedIsActive, categories);

        //when
        when(this.genreGateway.create(any())).thenAnswer(returnsFirstArg());
        final CreateGenreOutput execute = this.useCase.execute(aCommand);

        //then
        assertNotNull(execute);
        assertNotNull(execute.id());
        verify(this.genreGateway, times(1))
                .create(argThat(genre -> {
                    return Objects.equals(expectedName, genre.getName())
                            && Objects.equals(expectedIsActive, genre.isActive())
                            && Objects.nonNull(genre.getId())
                            && Objects.nonNull(genre.getCategories())
                            && Objects.nonNull(genre.getCreatedAt())
                            && Objects.nonNull(genre.getUpdatedAt())
                            && Objects.isNull(genre.getDeletedAt());
                }));
    }

    @Test
    void givenAValidCommandWithCategories_whenCallsCreateGenre_thenReturnGenreId() {
        //given
        final String expectedName = "Animes";
        final Boolean expectedIsActive = true;
        final CategoryId expectedCategoryId = CategoryId.unique();
        final int expectedCategoryIdCount = 1;
        final List<String> categories = List.of(expectedCategoryId.getValue());
        final CreateGenreCommand aCommand =
                CreateGenreCommand.with(expectedName, expectedIsActive, categories);

        //when
        when(this.categoryGateway.existsByIds(any())).thenReturn(List.of(expectedCategoryId));
        when(this.genreGateway.create(any())).thenAnswer(returnsFirstArg());
        final CreateGenreOutput execute = this.useCase.execute(aCommand);

        //then
        assertNotNull(execute);
        assertNotNull(execute.id());
        verify(this.categoryGateway, times(1)).existsByIds(any());
        verify(this.genreGateway, times(1))
                .create(argThat(genre -> {
                    return Objects.equals(expectedName, genre.getName())
                            && Objects.equals(expectedIsActive, genre.isActive())
                            && Objects.nonNull(genre.getId())
                            && Objects.nonNull(genre.getCategories())
                            && Objects.equals(expectedCategoryIdCount, genre.getCategories().size())
                            && Objects.equals(expectedCategoryId, genre.getCategories().get(0))
                            && Objects.nonNull(genre.getCreatedAt())
                            && Objects.nonNull(genre.getUpdatedAt())
                            && Objects.isNull(genre.getDeletedAt());
                }));
    }

}