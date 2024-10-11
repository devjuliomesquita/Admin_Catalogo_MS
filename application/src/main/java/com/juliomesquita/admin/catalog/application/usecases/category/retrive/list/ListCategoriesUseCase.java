package com.juliomesquita.admin.catalog.application.usecases.category.retrive.list;

import com.juliomesquita.admin.catalog.application.commom.UseCase;
import com.juliomesquita.admin.catalog.domain.commom.pagination.CategorySearchQuery;
import com.juliomesquita.admin.catalog.domain.commom.pagination.Pagination;

public abstract class ListCategoriesUseCase
        extends UseCase<CategorySearchQuery, Pagination<ListCategoriesOutput>> {
}
