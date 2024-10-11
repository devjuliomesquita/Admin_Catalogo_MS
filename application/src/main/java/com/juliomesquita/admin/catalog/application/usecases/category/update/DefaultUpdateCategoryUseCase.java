package com.juliomesquita.admin.catalog.application.usecases.category.update;

import com.juliomesquita.admin.catalog.domain.category.Category;
import com.juliomesquita.admin.catalog.domain.category.CategoryGateway;
import com.juliomesquita.admin.catalog.domain.category.CategoryId;
import com.juliomesquita.admin.catalog.domain.commom.exceptions.DomainException;
import com.juliomesquita.admin.catalog.domain.commom.validation.Error;
import com.juliomesquita.admin.catalog.domain.commom.validation.Notification;
import io.vavr.API;
import io.vavr.control.Either;

import java.util.Objects;

public class DefaultUpdateCategoryUseCase extends UpdateCategoryUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultUpdateCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Either<Notification, UpdateCategoryOutput> execute(UpdateCategoryCommand aCommand) {
        Category aCategory = this.categoryGateway.findById(CategoryId.from(aCommand.id()))
                .orElseThrow(() -> DomainException
                        .with(new Error("Category with ID %s was not found".formatted(aCommand.id()))));
        final Notification notification = Notification.create();
        aCategory
                .update(aCommand.name(), aCommand.description(), aCommand.isActive())
                .validate(notification);
        return notification.booleanHasError() ? API.Left(notification) : this.update(aCategory);
    }
    
    private Either<Notification, UpdateCategoryOutput> update(final Category aCategory) {
        return API.Try(() -> this.categoryGateway.update(aCategory))
                .toEither()
                .bimap(Notification::create, UpdateCategoryOutput::from);
    }
}
