package com.juliomesquita.admin.catalog.application.usecases.genre.create;

import java.util.List;

public record CreateGenreCommand(
        String name,
        boolean isActive,
        List<String> aCategories
) {
    public static CreateGenreCommand with(
            final String aName,
            final Boolean isActive,
            final List<String> aCategories
    ) {
        return new CreateGenreCommand(aName, isActive != null ? isActive : true, aCategories);
    }
}
