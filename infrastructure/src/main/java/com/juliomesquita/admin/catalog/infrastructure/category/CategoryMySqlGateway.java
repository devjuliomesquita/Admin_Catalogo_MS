package com.juliomesquita.admin.catalog.infrastructure.category;

import com.juliomesquita.admin.catalog.domain.category.Category;
import com.juliomesquita.admin.catalog.domain.category.CategoryGateway;
import com.juliomesquita.admin.catalog.domain.category.CategoryId;
import com.juliomesquita.admin.catalog.domain.commom.pagination.SearchQuery;
import com.juliomesquita.admin.catalog.domain.commom.pagination.Pagination;
import com.juliomesquita.admin.catalog.infrastructure.category.persistence.CategoryEntity;
import com.juliomesquita.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.juliomesquita.admin.catalog.infrastructure.utils.SpecificationUtils.like;

@Service
public class CategoryMySqlGateway implements CategoryGateway {
    private final CategoryRepository categoryRepository;

    public CategoryMySqlGateway(final CategoryRepository categoryRepository) {
        this.categoryRepository = Objects.requireNonNull(categoryRepository);
    }

    @Override
    public Category create(final Category aCategory) {
        return this.categoryRepository
                .save(CategoryEntity.from(aCategory))
                .toAggregate();
    }

    @Override
    public Optional<Category> findById(final CategoryId categoryId) {
        return this.categoryRepository
                .findById(categoryId.getValue())
                .map(CategoryEntity::toAggregate);
    }

    @Override
    public Pagination<Category> findAll(final SearchQuery aQuery) {
        final PageRequest pageRequest = PageRequest.of(
                aQuery.currentPage(),
                aQuery.itemsPerPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final Specification<CategoryEntity> specification = Optional.ofNullable(aQuery.terms())
                .filter(str -> !str.isBlank())
                .map(str -> {
                    final Specification<CategoryEntity> nameLike = like("name", str);
                    final Specification<CategoryEntity> descriptionLike = like("description", str);
                    return nameLike.or(descriptionLike);
                })
                .orElse(null);


        final Page<CategoryEntity> categoryPageable = this.categoryRepository.findAll(specification, pageRequest);
        return new Pagination<>(
                categoryPageable.map(CategoryEntity::toAggregate).toList(),
                categoryPageable.getNumber(),
                categoryPageable.getSize(),
                categoryPageable.getTotalElements(),
                (int) categoryPageable.getTotalElements() / categoryPageable.getSize()
        );
    }

    @Override
    public Category update(final Category aCategory) {
        return this.categoryRepository
                .save(CategoryEntity.from(aCategory))
                .toAggregate();
    }

    @Override
    public void deleteById(CategoryId categoryId) {
        String anId = categoryId.getValue();
        if (this.categoryRepository.existsById(anId)) {
            this.categoryRepository.deleteById(anId);
        }
    }

    @Override
    public List<CategoryId> existsByIds(Iterable<CategoryId> ids) {
        //TODO Implementar quando chegar na camada de infrastructure
        return List.of();
    }
}
