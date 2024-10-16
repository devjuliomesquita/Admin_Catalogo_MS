package com.juliomesquita.admin.catalog.infrastructure.api;

import com.juliomesquita.admin.catalog.domain.commom.pagination.Pagination;
import com.juliomesquita.admin.catalog.infrastructure.api.models.CategoryResponse;
import com.juliomesquita.admin.catalog.infrastructure.api.models.CreateCategoryRequest;
import com.juliomesquita.admin.catalog.infrastructure.api.models.ListCategoriesResponse;
import com.juliomesquita.admin.catalog.infrastructure.api.models.UpdateCategoryRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "/categories")
@Tag(name = "Categories")
public interface CategoryAPI {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createCategory(@RequestBody CreateCategoryRequest input);

    @GetMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CategoryResponse> getCategoryById(@PathVariable(name = "id") String id);

    @GetMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Pagination<ListCategoriesResponse>> ListCategories(
            @RequestParam(name = "search", required = false, defaultValue = "") final String search,
            @RequestParam(name = "currentPage", required = false, defaultValue = "0") final int currentPage,
            @RequestParam(name = "itemsPerPage", required = false, defaultValue = "10") final int itemsPerPage,
            @RequestParam(name = "sort", required = false, defaultValue = "name") final String sort,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") final String direction);

    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> updateCategory(
            @PathVariable(name = "id") String id,
            @RequestBody UpdateCategoryRequest input);

    @DeleteMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> deleteCategory(
            @PathVariable(name = "id") String id
    );
}
