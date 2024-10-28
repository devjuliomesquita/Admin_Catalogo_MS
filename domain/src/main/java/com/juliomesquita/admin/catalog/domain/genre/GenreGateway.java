package com.juliomesquita.admin.catalog.domain.genre;

import com.juliomesquita.admin.catalog.domain.commom.pagination.Pagination;
import com.juliomesquita.admin.catalog.domain.commom.pagination.SearchQuery;

import java.util.Optional;

public interface GenreGateway {
    Genre create(Genre aGenre);

    Optional<Genre> findById(String anId);

    Pagination<Genre> findAll(SearchQuery aQuery);

    Genre update(Genre aGenre);

    void delete(String anId);
}
