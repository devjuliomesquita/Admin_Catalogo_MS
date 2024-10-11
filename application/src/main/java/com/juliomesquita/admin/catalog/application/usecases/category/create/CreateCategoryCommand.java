package com.juliomesquita.admin.catalog.application.usecases.category.create;

public record CreateCategoryCommand(
        String name,
        String description,
        boolean isActive
) {
    public static CreateCategoryCommand with(
            final String aName,
            final String aDescription,
            final boolean aIsActive
    ) {
        return new CreateCategoryCommand(aName, aDescription, aIsActive);
    }
}
