package com.juliomesquita.admin.catalog.application.usecases.category.update;

public record UpdateCategoryCommand(
        String id,
        String name,
        String description,
        boolean isActive
) {
    public static UpdateCategoryCommand with(
            final String aId,
            final String aName,
            final String aDescription,
            final boolean anIsActive
    ){
        return new UpdateCategoryCommand(aId, aName, aDescription, anIsActive);
    }
}
