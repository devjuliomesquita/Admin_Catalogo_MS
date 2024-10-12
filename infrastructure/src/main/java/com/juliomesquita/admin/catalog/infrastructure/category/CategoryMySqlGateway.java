package com.juliomesquita.admin.catalog.infrastructure.category;

import com.juliomesquita.admin.catalog.domain.category.Category;
import com.juliomesquita.admin.catalog.domain.category.CategoryGateway;
import com.juliomesquita.admin.catalog.domain.category.CategoryId;
import com.juliomesquita.admin.catalog.domain.commom.pagination.CategorySearchQuery;
import com.juliomesquita.admin.catalog.domain.commom.pagination.Pagination;
import com.juliomesquita.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class CategoryMySqlGateway implements CategoryGateway {
    private final CategoryRepository categoryRepository;

    public CategoryMySqlGateway(final CategoryRepository categoryRepository) {
        this.categoryRepository = Objects.requireNonNull(categoryRepository);
    }

    @Override
    public Category create(Category aCategory) {
        return null;
    }

    @Override
    public Optional<Category> findById(CategoryId categoryId) {
        return Optional.empty();
    }

    @Override
    public Pagination<Category> findAll(CategorySearchQuery aQuery) {
        return null;
    }

    @Override
    public Category update(Category aCategory) {
        return null;
    }

    @Override
    public void deleteById(CategoryId categoryId) {

    }
}
