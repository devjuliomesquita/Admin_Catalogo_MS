package com.juliomesquita.admin.catalog.infrastructure.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.juliomesquita.admin.catalog.application.usecases.category.create.CreateCategoryCommand;
import com.juliomesquita.admin.catalog.application.usecases.category.create.CreateCategoryOutput;
import com.juliomesquita.admin.catalog.application.usecases.category.create.CreateCategoryUseCase;
import com.juliomesquita.admin.catalog.application.usecases.category.delete.DeleteCategoryUseCase;
import com.juliomesquita.admin.catalog.application.usecases.category.retrive.getbyid.GetCategoryByIdUseCase;
import com.juliomesquita.admin.catalog.application.usecases.category.retrive.list.ListCategoriesUseCase;
import com.juliomesquita.admin.catalog.application.usecases.category.update.UpdateCategoryUseCase;
import com.juliomesquita.admin.catalog.infrastructure.ControllerTest;
import com.juliomesquita.admin.catalog.infrastructure.api.CategoryAPI;
import com.juliomesquita.admin.catalog.infrastructure.api.models.CreateCategoryAPIInput;
import io.vavr.API;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/categories/123"));

        //then
        verify(this.createCategoryUseCase, times(1)).execute(
                argThat(cmd ->
                        Objects.equals(expectedName, cmd.name()) &&
                                Objects.equals(expectedDescription, cmd.description()) &&
                                Objects.equals(expectedIsActive, cmd.isActive())));
    }


}