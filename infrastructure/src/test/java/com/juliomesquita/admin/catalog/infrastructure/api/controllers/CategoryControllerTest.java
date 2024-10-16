package com.juliomesquita.admin.catalog.infrastructure.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.juliomesquita.admin.catalog.application.usecases.category.create.CreateCategoryOutput;
import com.juliomesquita.admin.catalog.application.usecases.category.create.CreateCategoryUseCase;
import com.juliomesquita.admin.catalog.application.usecases.category.delete.DeleteCategoryUseCase;
import com.juliomesquita.admin.catalog.application.usecases.category.retrive.getbyid.CategoryOutput;
import com.juliomesquita.admin.catalog.application.usecases.category.retrive.getbyid.GetCategoryByIdUseCase;
import com.juliomesquita.admin.catalog.application.usecases.category.retrive.list.ListCategoriesOutput;
import com.juliomesquita.admin.catalog.application.usecases.category.retrive.list.ListCategoriesUseCase;
import com.juliomesquita.admin.catalog.application.usecases.category.update.UpdateCategoryOutput;
import com.juliomesquita.admin.catalog.application.usecases.category.update.UpdateCategoryUseCase;
import com.juliomesquita.admin.catalog.domain.category.Category;
import com.juliomesquita.admin.catalog.domain.category.CategoryId;
import com.juliomesquita.admin.catalog.domain.commom.exceptions.DomainException;
import com.juliomesquita.admin.catalog.domain.commom.exceptions.NotFoundException;
import com.juliomesquita.admin.catalog.domain.commom.pagination.Pagination;
import com.juliomesquita.admin.catalog.domain.commom.validation.Error;
import com.juliomesquita.admin.catalog.domain.commom.validation.Notification;
import com.juliomesquita.admin.catalog.infrastructure.ControllerTest;
import com.juliomesquita.admin.catalog.infrastructure.api.CategoryAPI;
import com.juliomesquita.admin.catalog.infrastructure.api.models.CreateCategoryRequest;
import com.juliomesquita.admin.catalog.infrastructure.api.models.UpdateCategoryRequest;
import io.vavr.API;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = CategoryAPI.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateCategoryUseCase createCategoryUseCase;
    @MockBean
    private GetCategoryByIdUseCase getCategoryByIdUseCase;
    @MockBean
    private ListCategoriesUseCase listCategoriesUseCase;
    @MockBean
    private UpdateCategoryUseCase updateCategoryUseCase;
    @MockBean
    private DeleteCategoryUseCase deleteCategoryUseCase;

    @Test
    void givenAValidCommand_whenCallsCreateCategory_thenReturnCategoryId() throws Exception {
        //given
        final String expectedName = "Terror";
        final String expectedDescription = "Categoria mais assistida.";
        final boolean expectedIsActive = true;

        final CreateCategoryRequest input =
                new CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        //mockUp da requisição
        final MockHttpServletRequestBuilder request = post("/categories")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(this.mapper.writeValueAsBytes(input));

        //when
        when(this.createCategoryUseCase.execute(any()))
                .thenReturn(API.Right(CreateCategoryOutput.from("123")));
        this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/categories/123"));

        //then
        verify(this.createCategoryUseCase, times(1)).execute(
                argThat(cmd ->
                        Objects.equals(expectedName, cmd.name()) &&
                                Objects.equals(expectedDescription, cmd.description()) &&
                                Objects.equals(expectedIsActive, cmd.isActive())));
    }

    @Test
    void givenAInvalidCommand_whenCallsCreateCategory_thenReturnNotificationError() throws Exception {
        //given
        final String expectedName = null;
        final String expectedDescription = "Categoria mais assistida.";
        final String expectedMessageError = "'name' should be not null";
        final boolean expectedIsActive = true;

        final CreateCategoryRequest input =
                new CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        //mockUp da requisição
        final MockHttpServletRequestBuilder request = post("/categories")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(this.mapper.writeValueAsBytes(input));

        //when
        when(this.createCategoryUseCase.execute(any()))
                .thenReturn(API.Left(Notification.create(new Error(expectedMessageError))));
        this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessageError)))
                .andExpect(jsonPath("$.errors", hasSize(1)));

        //then
        verify(this.createCategoryUseCase, times(1)).execute(
                argThat(cmd ->
                        Objects.equals(expectedName, cmd.name()) &&
                                Objects.equals(expectedDescription, cmd.description()) &&
                                Objects.equals(expectedIsActive, cmd.isActive())));
    }

    @Test
    void givenAInvalidCommand_whenCallsCreateCategory_thenReturnDomainException() throws Exception {
        //given
        final String expectedName = null;
        final String expectedDescription = "Categoria mais assistida.";
        final String expectedMessageError = "'name' should be not null";
        final boolean expectedIsActive = true;

        final CreateCategoryRequest input =
                new CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        //mockUp da requisição
        final MockHttpServletRequestBuilder request = post("/categories")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(this.mapper.writeValueAsBytes(input));

        //when
        when(this.createCategoryUseCase.execute(any()))
                .thenThrow(DomainException.with(new Error(expectedMessageError)));
        this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessageError)))
                .andExpect(jsonPath("$.errors", hasSize(1)));

        //then
        verify(this.createCategoryUseCase, times(1)).execute(
                argThat(cmd ->
                        Objects.equals(expectedName, cmd.name()) &&
                                Objects.equals(expectedDescription, cmd.description()) &&
                                Objects.equals(expectedIsActive, cmd.isActive())));
    }

    @Test
    void givenAValidParamId_whenCallsGetByIdCategory_thenReturnCategory() throws Exception {
        //given
        final String expectedName = "Terror";
        final String expectedDescription = "Categoria mais assistida.";
        final boolean expectedIsActive = true;
        final Category aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final String expectedId = aCategory.getId().getValue();

        //mockUp da requisição
        final MockHttpServletRequestBuilder request = get("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        //when
        when(this.getCategoryByIdUseCase.execute(any())).thenReturn(CategoryOutput.from(aCategory));
        final ResultActions response = this.mockMvc
                .perform(request)
                .andDo(print());

        //then
        response
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)))
                .andExpect(jsonPath("$.name", equalTo(expectedName)))
                .andExpect(jsonPath("$.description", equalTo(expectedDescription)))
                .andExpect(jsonPath("$.is_active", equalTo(expectedIsActive)))
                .andExpect(jsonPath("$.created_at", equalTo(aCategory.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updated_at", equalTo(aCategory.getUpdatedAt().toString())))
                .andExpect(jsonPath("$.deleted_at", equalTo(aCategory.getDeletedAt())));
        verify(this.getCategoryByIdUseCase, times(1)).execute(any());
    }

    @Test
    void givenAInvalidParamId_whenCallsGetByIdCategory_thenReturnNotFound() throws Exception {
        //given
        final String expectedMessageError = "Category with ID 9ae8e82d-330b-403b-b4c8-299e1a526c46 was not found";
        final String expectedId = "9ae8e82d-330b-403b-b4c8-299e1a526c46";

        //mockUp da requisição
        final MockHttpServletRequestBuilder request = get("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        //when
        when(this.getCategoryByIdUseCase.execute(any())).thenThrow(
                NotFoundException.with(Category.class, CategoryId.from(expectedId)));
        final ResultActions response = this.mockMvc.perform(request).andDo(print());

        //then
        response
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo(expectedMessageError)));
        verify(this.getCategoryByIdUseCase, times(1)).execute(any());
    }

    @Test
    void givenAValidParams_whenCallsUpdateCategory_thenReturnCategoryId() throws Exception {
        //given
        final String expectedId = "868ca2fc-fed8-441e-8ccb-9cae4c69ba10";
        final String expectedName = "Terror";
        final String expectedDescription = "Categoria mais assistida.";
        final boolean expectedIsActive = true;

        final UpdateCategoryRequest input =
                new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        //mockUp da requisição
        final MockHttpServletRequestBuilder request = put("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(this.mapper.writeValueAsBytes(input));

        //when
        when(this.updateCategoryUseCase.execute(any()))
                .thenReturn(API.Right(UpdateCategoryOutput.from(expectedId)));
        final ResultActions response = this.mockMvc.perform(request)
                .andDo(print());

        //then
        response
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)));

        verify(this.updateCategoryUseCase, times(1)).execute(
                argThat(cmd ->
                        Objects.equals(expectedName, cmd.name()) &&
                                Objects.equals(expectedDescription, cmd.description()) &&
                                Objects.equals(expectedIsActive, cmd.isActive())));
    }

    @Test
    void givenAValidCommand_whenGatewayThrowsRandomException_thenReturnAnException() throws Exception {
        //given
        final String expectedId = "868ca2fc-fed8-441e-8ccb-9cae4c69ba10";
        final String expectedName = "Terror";
        final String expectedDescription = "Categoria mais assistida.";
        final String expectedMessageError = "Erro inesperado";
        final boolean expectedIsActive = true;

        final UpdateCategoryRequest input =
                new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        //mockUp da requisição
        final MockHttpServletRequestBuilder request = put("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(this.mapper.writeValueAsBytes(input));

        //when
        when(this.updateCategoryUseCase.execute(any()))
                .thenReturn(API.Left(Notification.create(new Error(expectedMessageError))));
        final ResultActions response = this.mockMvc.perform(request)
                .andDo(print());

        //then
        response
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessageError)))
                .andExpect(jsonPath("$.errors", hasSize(1)));
        verify(this.updateCategoryUseCase, times(1)).execute(any());
    }

    @Test
    void givenACommandWithInvalidId_whenCallsUpdateCategory_thenReturnNotFoundException() throws Exception {
        //given
        final String expectedId = "868ca2fc-fed8-441e-8ccb-9cae4c69ba10";
        final String expectedName = "Terror";
        final String expectedDescription = "Categoria mais assistida.";
        final String expectedMessageError = "Category with ID 868ca2fc-fed8-441e-8ccb-9cae4c69ba10 was not found";
        final boolean expectedIsActive = true;

        final UpdateCategoryRequest input =
                new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        //mockUp da requisição
        final MockHttpServletRequestBuilder request = put("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(this.mapper.writeValueAsBytes(input));

        //when
        when(this.updateCategoryUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Category.class, CategoryId.from(expectedId)));
        final ResultActions response = this.mockMvc.perform(request)
                .andDo(print());

        //then
        response
                .andExpect(status().isNotFound())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(jsonPath("$.message", equalTo(expectedMessageError)));
        verify(this.updateCategoryUseCase, times(1)).execute(
                argThat(cmd ->
                        Objects.equals(expectedId, cmd.id()) &&
                                Objects.equals(expectedName, cmd.name()) &&
                                Objects.equals(expectedDescription, cmd.description()) &&
                                Objects.equals(expectedIsActive, cmd.isActive()))
        );
    }

    @Test
    void givenAValidParamId_whenCallsDeleteCategory_thenReturnVoid() throws Exception {
        //given
        final String expectedId = "868ca2fc-fed8-441e-8ccb-9cae4c69ba10";

        //mockUp da requisição
        final MockHttpServletRequestBuilder request = delete("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        //when
        doNothing()
                .when(this.deleteCategoryUseCase)
                .execute(expectedId);
        final ResultActions response = this.mockMvc.perform(request)
                .andDo(print());

        //then
        response
                .andExpect(status().isNoContent())
                .andExpect(header().string("Location", nullValue()));
        verify(this.deleteCategoryUseCase, times(1)).execute(argThat(
                cmd -> Objects.equals(expectedId, cmd)));
    }

    @Test
    void givenAValidParamsTerms_whenCallsListCategories_thenReturnCategoriesFiltered() throws Exception {
        //given
        final Category aCategory = Category.newCategory("Filmes", "De arrepiar a  espinha", true);
        final int expectedCurrentPage = 0;
        final int expectedItemsPerPage = 10;
        final String expectedTerms = "filmes";
        final String expectedSort = "name";
        final String expectedDirection = "asc";
        final int expectedItemsCount = 1;
        final int expectedTotalItems = 1;
        final int expectedTotalPages = expectedTotalItems / expectedItemsPerPage;
        final List<ListCategoriesOutput> categories = Stream.of(aCategory).map(ListCategoriesOutput::from).toList();

        //mockUp da requisição
        final MockHttpServletRequestBuilder request = get("/categories")
                .queryParam("search", expectedTerms)
                .queryParam("currentPage", String.valueOf(expectedCurrentPage))
                .queryParam("itemsPerPage", String.valueOf(expectedItemsPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("direction", expectedDirection)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        //when
        when(this.listCategoriesUseCase.execute(any()))
                .thenReturn(new Pagination<>(
                        categories,
                        expectedCurrentPage,
                        expectedItemsPerPage,
                        expectedItemsCount,
                        (expectedTotalItems / expectedItemsPerPage))
                );
        final ResultActions response = this.mockMvc.perform(request).andDo(print());

        //then
        response
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.current_page", equalTo(expectedCurrentPage)))
                .andExpect(jsonPath("$.items_per_page", equalTo(expectedItemsPerPage)))
                .andExpect(jsonPath("$.total_items", equalTo(expectedTotalItems)))
                .andExpect(jsonPath("$.total_pages", equalTo(expectedTotalPages)))
                .andExpect(jsonPath("$.items", hasSize(expectedTotalItems)))
                .andExpect(jsonPath("$.items[0].id", equalTo(aCategory.getId().getValue())))
                .andExpect(jsonPath("$.items[0].name", equalTo(aCategory.getName())))
                .andExpect(jsonPath("$.items[0].description", equalTo(aCategory.getDescription())))
                .andExpect(jsonPath("$.items[0].is_active", equalTo(aCategory.isActive())))
                .andExpect(jsonPath("$.items[0].created_at", notNullValue()));

        verify(this.listCategoriesUseCase, times(1)).execute(argThat(
                cmd ->
                        Objects.equals(expectedCurrentPage, cmd.currentPage()) &&
                                Objects.equals(expectedItemsPerPage, cmd.itemsPerPage()) &&
                                Objects.equals(expectedTerms, cmd.terms()) &&
                                Objects.equals(expectedSort, cmd.sort()) &&
                                Objects.equals(expectedDirection, cmd.direction()))
        );
    }
}