package com.juliomesquita.admin.catalog.infrastructure.api.controllers;

import com.juliomesquita.admin.catalog.application.usecases.category.create.CreateCategoryCommand;
import com.juliomesquita.admin.catalog.application.usecases.category.create.CreateCategoryOutput;
import com.juliomesquita.admin.catalog.application.usecases.category.create.CreateCategoryUseCase;
import com.juliomesquita.admin.catalog.application.usecases.category.delete.DeleteCategoryUseCase;
import com.juliomesquita.admin.catalog.application.usecases.category.retrive.getbyid.GetCategoryByIdUseCase;
import com.juliomesquita.admin.catalog.application.usecases.category.retrive.list.ListCategoriesUseCase;
import com.juliomesquita.admin.catalog.application.usecases.category.update.UpdateCategoryCommand;
import com.juliomesquita.admin.catalog.application.usecases.category.update.UpdateCategoryOutput;
import com.juliomesquita.admin.catalog.application.usecases.category.update.UpdateCategoryUseCase;
import com.juliomesquita.admin.catalog.domain.commom.pagination.SearchQuery;
import com.juliomesquita.admin.catalog.domain.commom.pagination.Pagination;
import com.juliomesquita.admin.catalog.domain.commom.validation.Notification;
import com.juliomesquita.admin.catalog.infrastructure.api.CategoryAPI;
import com.juliomesquita.admin.catalog.infrastructure.api.models.CategoryResponse;
import com.juliomesquita.admin.catalog.infrastructure.api.models.CreateCategoryRequest;
import com.juliomesquita.admin.catalog.infrastructure.api.models.ListCategoriesResponse;
import com.juliomesquita.admin.catalog.infrastructure.api.models.UpdateCategoryRequest;
import com.juliomesquita.admin.catalog.infrastructure.api.presenters.CategoryApiPresenter;
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
    public ResponseEntity<?> createCategory(final CreateCategoryRequest input) {
        final CreateCategoryCommand command = CreateCategoryRequest.toApp(input);
        final Function<Notification, ResponseEntity<?>> onError =
                notification -> ResponseEntity.unprocessableEntity().body(notification);
        final Function<CreateCategoryOutput, ResponseEntity<?>> onSuccess =
                output -> ResponseEntity.created(URI.create("/categories/" + output.id())).body(output);

        return this.createCategoryUseCase.execute(command).fold(onError, onSuccess);
    }

    @Override
    public ResponseEntity<CategoryResponse> getCategoryById(final String id) {
        final CategoryResponse response = CategoryApiPresenter.presentSimple
                .compose(this.getCategoryByIdUseCase::execute)
                .apply(id);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Pagination<ListCategoriesResponse>> ListCategories(
            final String search,
            final int currentPage,
            final int itemsPerPage,
            final String sort,
            final String direction
    ) {
        Pagination<ListCategoriesResponse> response = this.listCategoriesUseCase
                .execute(new SearchQuery(currentPage, itemsPerPage, search, sort, direction))
                .map(CategoryApiPresenter.presentList);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> updateCategory(final String id, final UpdateCategoryRequest input) {
        final UpdateCategoryCommand aCommand = UpdateCategoryRequest.toApp(id, input);
        final Function<Notification, ResponseEntity<?>> onError =
                notification -> ResponseEntity.unprocessableEntity().body(notification);
        final Function<UpdateCategoryOutput, ResponseEntity<?>> onSuccess =
                ResponseEntity::ok;
        return this.updateCategoryUseCase.execute(aCommand).fold(onError, onSuccess);
    }

    @Override
    public ResponseEntity<?> deleteCategory(final String id) {
        this.deleteCategoryUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
