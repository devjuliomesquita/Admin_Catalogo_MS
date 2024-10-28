package com.juliomesquita.admin.catalog.application.usecases.genre.create;

import com.juliomesquita.admin.catalog.domain.category.Category;
import com.juliomesquita.admin.catalog.domain.category.CategoryId;
import com.juliomesquita.admin.catalog.domain.genre.Genre;
import com.juliomesquita.admin.catalog.domain.genre.GenreId;

public record CreateGenreOutput(
        String id
) {
    public static CreateGenreOutput from(Genre aGenre) {
        return new CreateGenreOutput(aGenre.getId().getValue());
    }

    public static CreateGenreOutput from(String anId) {
        return new CreateGenreOutput(GenreId.from(anId).getValue());
    }
}
