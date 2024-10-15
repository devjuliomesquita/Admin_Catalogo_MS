package com.juliomesquita.admin.catalog.infrastructure.api.controllers;

import com.juliomesquita.admin.catalog.application.usecases.category.create.CreateCategoryCommand;
import com.juliomesquita.admin.catalog.application.usecases.category.create.CreateCategoryOutput;
import com.juliomesquita.admin.catalog.application.usecases.category.create.CreateCategoryUseCase;
import com.juliomesquita.admin.catalog.application.usecases.category.delete.DeleteCategoryUseCase;
import com.juliomesquita.admin.catalog.application.usecases.category.retrive.getbyid.GetCategoryByIdUseCase;
import com.juliomesquita.admin.catalog.application.usecases.category.retrive.list.ListCategoriesUseCase;
import com.juliomesquita.admin.catalog.application.usecases.category.update.UpdateCategoryUseCase;
import com.juliomesquita.admin.catalog.domain.commom.pagination.Pagination;
import com.juliomesquita.admin.catalog.domain.commom.validation.Notification;
import com.juliomesquita.admin.catalog.infrastructure.api.CategoryAPI;
import com.juliomesquita.admin.catalog.infrastructure.api.models.CreateCategoryAPIInput;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;

@RestController
public class CategoryController implements CategoryAPI {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final GetCategoryByIdUseCase getCategoryByIdUseCase;
    private final ListCategoriesUseCase listCategoriesUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;

    public CategoryController(
            final CreateCategoryUseCase createCategoryUseCase,
            final GetCategoryByIdUseCase getCategoryByIdUseCase,
            final ListCategoriesUseCase listCategoriesUseCase,
            final UpdateCategoryUseCase updateCategoryUseCase,
            final DeleteCategoryUseCase deleteCategoryUseCase
    ) {
        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
        this.getCategoryByIdUseCase = Objects.requireNonNull(getCategoryByIdUseCase);
        this.listCategoriesUseCase = Objects.requireNonNull(listCategoriesUseCase);
        this.updateCategoryUseCase = Objects.requireNonNull(updateCategoryUseCase);
        this.deleteCategoryUseCase = Objects.requireNonNull(deleteCategoryUseCase);
    }

    @Override
    public ResponseEntity<?> createCategory(final CreateCategoryAPIInput input) {
        final CreateCategoryCommand command = CreateCategoryAPIInput.toApp(input);
        final Function<Notification, ResponseEntity<?>> onError =
                notification -> ResponseEntity.unprocessableEntity().body(notification);
        final Function<CreateCategoryOutput, ResponseEntity<?>> onSuccess =
                output -> ResponseEntity.created(URI.create("/categories/" + output.id())).body(output);

        return this.createCategoryUseCase.execute(command).fold(onError, onSuccess);
    }

    @Override
    public ResponseEntity<Pagination<?>> ListCategories(String search, int currentPage, int itemsPerPage, String sort, String direction) {
        return null;
    }
}
