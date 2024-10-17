package com.juliomesquita.admin.catalog.e2e.category;

import com.juliomesquita.admin.catalog.domain.category.CategoryId;
import com.juliomesquita.admin.catalog.infrastructure.api.models.CreateCategoryRequest;
import com.juliomesquita.admin.catalog.infrastructure.api.models.UpdateCategoryRequest;
import com.juliomesquita.admin.catalog.infrastructure.configuration.mapper.json.Json;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public interface MockDslE2E {
    MockMvc mockMvc();

    //Categories
    default CategoryId givenCategory(
            final String aName,
            final String aDescription,
            final boolean aIsActive
    ) throws Exception {
        final CreateCategoryRequest input =
                new CreateCategoryRequest(aName, aDescription, aIsActive);
        final String anIdCreated = this.given("/categories", input);
        return CategoryId.from(anIdCreated);
    }

    default ResultActions retrievedACategory(final String anId) throws Exception {
        return this.retrieve("/categories/", anId);
    }

    default ResultActions listCategories(
            final int currentPage,
            final int itemsPerPage
    ) throws Exception {
        return this.list("/categories", "", currentPage, itemsPerPage, "", "");
    }

    default ResultActions listCategories(
            final String search,
            final int currentPage,
            final int itemsPerPage
    ) throws Exception {
        return this.list("/categories", search, currentPage, itemsPerPage, "", "");
    }

    default ResultActions listCategories(
            final String search,
            final int currentPage,
            final int itemsPerPage,
            final String sort,
            final String direction
    ) throws Exception {
        return this.list("/categories", search, currentPage, itemsPerPage, sort, direction);
    }

    default ResultActions updateCategory(final String anId, final UpdateCategoryRequest request) throws Exception {
        return this.update("/categories/", anId, request);
    }

    default ResultActions deleteACategory(final String anId) throws Exception {
        return this.delete("/categories/", anId);
    }

    private String given(final String url, final Object body) throws Exception {
        final MockHttpServletRequestBuilder request = post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(Json.writeValueAsString(body));
        return Objects.requireNonNull(this.mockMvc().perform(request)
                        .andExpect(status().isCreated())
                        .andReturn()
                        .getResponse()
                        .getHeader("Location"))
                .replace("%s/".formatted(url), "");
    }

    private ResultActions retrieve(final String url, final String anId) throws Exception {
        final MockHttpServletRequestBuilder request = get(url + anId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);
        return this.mockMvc().perform(request);
    }

    private ResultActions list(
            final String url,
            final String search,
            final int currentPage,
            final int itemsPerPage,
            final String sort,
            final String direction

    ) throws Exception {
        final MockHttpServletRequestBuilder request = get(url)
                .queryParam("search", search)
                .queryParam("currentPage", String.valueOf(currentPage))
                .queryParam("itemsPerPage", String.valueOf(itemsPerPage))
                .queryParam("sort", sort)
                .queryParam("direction", direction)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);
        return this.mockMvc().perform(request);
    }

    private ResultActions update(
            final String url,
            final String anId,
            final UpdateCategoryRequest categoryRequest
    ) throws Exception {
        final MockHttpServletRequestBuilder request = put(url + anId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(categoryRequest));

        return this.mockMvc().perform(request);
    }

    private ResultActions delete(final String url, final String anId) throws Exception {
        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete(url + anId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        return this.mockMvc().perform(request);
    }
}
