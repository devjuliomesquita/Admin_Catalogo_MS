package com.juliomesquita.admin.catalog.infrastructure.category.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, String> {
    Page<CategoryEntity> findAll(Specification<CategoryEntity> whereClause, Pageable pageable);
}
