package com.juliomesquita.admin.catalog.application.usecases.category.retrive.list;

import com.juliomesquita.admin.catalog.domain.category.CategoryGateway;
import com.juliomesquita.admin.catalog.domain.commom.pagination.SearchQuery;
import com.juliomesquita.admin.catalog.domain.commom.pagination.Pagination;

import java.util.Objects;

public class DefaultListCategoriesUseCase extends ListCategoriesUseCase {
    private final CategoryGateway categoryGateway;

    public DefaultListCategoriesUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Pagination<ListCategoriesOutput> execute(SearchQuery aCommand) {
        return this.categoryGateway.findAll(aCommand)
                .map(ListCategoriesOutput::from);
    }
}
