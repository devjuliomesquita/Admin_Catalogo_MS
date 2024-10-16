package com.juliomesquita.admin.catalog.infrastructure.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.juliomesquita.admin.catalog.application.usecases.category.create.CreateCategoryCommand;
import com.juliomesquita.admin.catalog.application.usecases.category.create.CreateCategoryOutput;
import com.juliomesquita.admin.catalog.application.usecases.category.create.CreateCategoryUseCase;
import com.juliomesquita.admin.catalog.application.usecases.category.delete.DeleteCategoryUseCase;
import com.juliomesquita.admin.catalog.application.usecases.category.retrive.getbyid.CategoryOutput;
import com.juliomesquita.admin.catalog.application.usecases.category.retrive.getbyid.GetCategoryByIdUseCase;
import com.juliomesquita.admin.catalog.application.usecases.category.retrive.list.ListCategoriesUseCase;
import com.juliomesquita.admin.catalog.application.usecases.category.update.UpdateCategoryUseCase;
import com.juliomesquita.admin.catalog.domain.category.Category;
import com.juliomesquita.admin.catalog.domain.category.CategoryId;
import com.juliomesquita.admin.catalog.domain.commom.exceptions.DomainException;
import com.juliomesquita.admin.catalog.domain.commom.exceptions.NotFoundException;
import com.juliomesquita.admin.catalog.domain.commom.validation.Error;
import com.juliomesquita.admin.catalog.domain.commom.validation.Notification;
import com.juliomesquita.admin.catalog.infrastructure.ControllerTest;
import com.juliomesquita.admin.catalog.infrastructure.api.CategoryAPI;
import com.juliomesquita.admin.catalog.infrastructure.api.models.CreateCategoryAPIInput;
import io.vavr.API;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Objects;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

        final CreateCategoryAPIInput input =
                new CreateCategoryAPIInput(expectedName, expectedDescription, expectedIsActive);

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

        final CreateCategoryAPIInput input =
                new CreateCategoryAPIInput(expectedName, expectedDescription, expectedIsActive);

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

        final CreateCategoryAPIInput input =
                new CreateCategoryAPIInput(expectedName, expectedDescription, expectedIsActive);

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
                .andExpect(jsonPath("$.create_at", equalTo(aCategory.getCreatedAt().toString())))
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
}