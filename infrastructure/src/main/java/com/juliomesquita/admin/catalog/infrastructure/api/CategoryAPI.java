package com.juliomesquita.admin.catalog.infrastructure.api;

import com.juliomesquita.admin.catalog.domain.commom.pagination.Pagination;
import com.juliomesquita.admin.catalog.infrastructure.api.models.CategoryAPIOutput;
import com.juliomesquita.admin.catalog.infrastructure.api.models.CreateCategoryAPIInput;
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
    ResponseEntity<?> createCategory(@RequestBody CreateCategoryAPIInput input);

    @GetMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE    )
    ResponseEntity<CategoryAPIOutput> getCategoryById(@PathVariable(name = "id") String id);

    @GetMapping
    ResponseEntity<Pagination<?>> ListCategories(
            @RequestParam(name = "search", required = false, defaultValue = "") final String search,
            @RequestParam(name = "currentPage", required = false, defaultValue = "0") final int currentPage,
            @RequestParam(name = "itemsPerPage", required = false, defaultValue = "10") final int itemsPerPage,
            @RequestParam(name = "sort", required = false, defaultValue = "name") final String sort,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") final String direction);
}
