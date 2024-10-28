package com.juliomesquita.admin.catalog.application.usecases.genre.create;

import com.juliomesquita.admin.catalog.domain.category.CategoryGateway;
import com.juliomesquita.admin.catalog.domain.category.CategoryId;
import com.juliomesquita.admin.catalog.domain.commom.exceptions.NotificationException;
import com.juliomesquita.admin.catalog.domain.commom.validation.Error;
import com.juliomesquita.admin.catalog.domain.commom.validation.Notification;
import com.juliomesquita.admin.catalog.domain.commom.validation.ValidationHandler;
import com.juliomesquita.admin.catalog.domain.genre.Genre;
import com.juliomesquita.admin.catalog.domain.genre.GenreGateway;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DefaultCreateGenreUseCase extends CreateGenreUseCase {
    private final GenreGateway genreGateway;
    private final CategoryGateway categoryGateway;

    public DefaultCreateGenreUseCase(
            final GenreGateway genreGateway,
            final CategoryGateway categoryGateway
    ) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public CreateGenreOutput execute(CreateGenreCommand aCommand) {
        final List<CategoryId> categoryIds = this.toCategoryId(aCommand.aCategories());
        final Notification notification = Notification.create();
        notification.append(this.validationCategories(categoryIds));

        final Genre aGenre = notification.validate(() -> Genre.newGenre(aCommand.name(), aCommand.isActive()));
        if (notification.booleanHasError()) {
            throw new NotificationException("Could not create Aggregate Genre", notification);
        }

        aGenre.addCategories(categoryIds);

        return CreateGenreOutput.from(this.genreGateway.create(aGenre));
    }

    private ValidationHandler validationCategories(final List<CategoryId> ids) {
        final Notification notification = Notification.create();
        if (ids == null || ids.isEmpty()) {
            return notification;
        }
        final List<CategoryId> retrievedIds = this.categoryGateway.existsByIds(ids);
        if (ids.size() != retrievedIds.size()) {
            final ArrayList<CategoryId> missingIds = new ArrayList<>(ids);
            missingIds.removeAll(retrievedIds);
            final String missingIdsMessage = missingIds.stream()
                    .map(CategoryId::getValue)
                    .collect(Collectors.joining(", "));
            notification.append(new Error("Some categories could not be found: %s".formatted(missingIdsMessage)));

        }
        return notification;
    }

    private List<CategoryId> toCategoryId(final List<String> anIds) {
        return anIds.stream()
                .map(CategoryId::from)
                .toList();
    }
}
