package com.juliomesquita.admin.catalog.domain.category;

import com.juliomesquita.admin.catalog.domain.commom.pagination.SearchQuery;
import com.juliomesquita.admin.catalog.domain.commom.pagination.Pagination;

import java.util.List;
import java.util.Optional;

public interface CategoryGateway {
    Category create(Category aCategory);

    Optional<Category> findById(CategoryId categoryId);

    Pagination<Category> findAll(SearchQuery aQuery);

    Category update(Category aCategory);

    void deleteById(CategoryId categoryId);

    List<CategoryId> existsByIds(Iterable<CategoryId> ids);
}
