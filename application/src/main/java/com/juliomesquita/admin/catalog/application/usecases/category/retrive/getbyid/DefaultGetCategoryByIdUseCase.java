package com.juliomesquita.admin.catalog.application.usecases.category.retrive.getbyid;

import com.juliomesquita.admin.catalog.domain.category.Category;
import com.juliomesquita.admin.catalog.domain.category.CategoryGateway;
import com.juliomesquita.admin.catalog.domain.category.CategoryId;
import com.juliomesquita.admin.catalog.domain.commom.exceptions.DomainException;
import com.juliomesquita.admin.catalog.domain.commom.exceptions.NotFoundException;
import com.juliomesquita.admin.catalog.domain.commom.validation.Error;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultGetCategoryByIdUseCase extends GetCategoryByIdUseCase {
    private final CategoryGateway categoryGateway;

    public DefaultGetCategoryByIdUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public CategoryOutput execute(final String aCommand) {
        final CategoryId categoryId = CategoryId.from(aCommand);
        return this.categoryGateway.findById(categoryId)
                .map(CategoryOutput::from)
                .orElseThrow(this.notFound(categoryId));
    }

    private Supplier<NotFoundException> notFound(final CategoryId anId) {
        return () -> NotFoundException.with(
                Category.class,
                anId);
    }

}
