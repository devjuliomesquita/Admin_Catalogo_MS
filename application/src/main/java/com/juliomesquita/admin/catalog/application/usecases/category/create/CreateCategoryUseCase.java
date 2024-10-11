package com.juliomesquita.admin.catalog.application.usecases.category.create;

import com.juliomesquita.admin.catalog.application.commom.UseCase;
import com.juliomesquita.admin.catalog.domain.commom.validation.Notification;
import io.vavr.control.Either;

public abstract class CreateCategoryUseCase
        extends UseCase<CreateCategoryCommand, Either<Notification, CreateCategoryOutput>> {
}
