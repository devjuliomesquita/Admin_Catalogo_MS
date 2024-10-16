package com.juliomesquita.admin.catalog.infrastructure.api.presenters;

import com.juliomesquita.admin.catalog.application.usecases.category.retrive.getbyid.CategoryOutput;
import com.juliomesquita.admin.catalog.infrastructure.api.models.CategoryAPIOutput;

import java.util.function.Function;

public interface CategoryApiPresenter {
    Function<CategoryOutput, CategoryAPIOutput> present =
            output -> new CategoryAPIOutput(
                    output.id(),
                    output.name(),
                    output.description(),
                    output.active(),
                    output.createdAt(),
                    output.updatedAt(),
                    output.deletedAt()
            );
}
