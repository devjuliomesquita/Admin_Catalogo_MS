package com.juliomesquita.admin.catalog.infrastructure.api.presenters;

import com.juliomesquita.admin.catalog.application.usecases.category.retrive.getbyid.CategoryOutput;
import com.juliomesquita.admin.catalog.application.usecases.category.retrive.list.ListCategoriesOutput;
import com.juliomesquita.admin.catalog.infrastructure.api.models.CategoryResponse;
import com.juliomesquita.admin.catalog.infrastructure.api.models.ListCategoriesResponse;

import java.util.function.Function;

public interface CategoryApiPresenter {
    Function<CategoryOutput, CategoryResponse> presentSimple =
            output -> new CategoryResponse(
                    output.id(),
                    output.name(),
                    output.description(),
                    output.active(),
                    output.createdAt(),
                    output.updatedAt(),
                    output.deletedAt()
            );

    Function<ListCategoriesOutput, ListCategoriesResponse> presentList =
            output -> new ListCategoriesResponse(
                    output.id(),
                    output.name(),
                    output.description(),
                    output.active(),
                    output.createdAt()
            );
}
