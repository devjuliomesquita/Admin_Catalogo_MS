package com.juliomesquita.admin.catalog.domain.category;


import com.juliomesquita.admin.catalog.domain.commom.abstractions.AggregateRoot;
import com.juliomesquita.admin.catalog.domain.commom.validation.ValidationHandler;

import java.time.Instant;

public class Category extends AggregateRoot<CategoryId> implements Cloneable {
    private String name;
    private String description;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    public static Category with(final Category aCategory) {
        return with(
                aCategory.getId(),
                aCategory.getName(),
                aCategory.getDescription(),
                aCategory.isActive(),
                aCategory.getCreatedAt(),
                aCategory.getUpdatedAt(),
                aCategory.getDeletedAt());
    }

    public static Category with(
            final CategoryId aId,
            final String aName,
            final String aDescription,
            final boolean isActive,
            final Instant aCreatedDate,
            final Instant aUpdatedDate,
            final Instant aDeletedDate
    ) {
        return new Category(aId, aName, aDescription, isActive, aCreatedDate, aUpdatedDate, aDeletedDate);
    }

    public static Category newCategory(
            final String aName, final String aDescription, final boolean aActive
    ) {
        final CategoryId id = CategoryId.unique();
        final Instant instant = Instant.now();
        final Instant deletedAt = aActive ? null : instant;
        return new Category(id, aName, aDescription, aActive, instant, instant, deletedAt);
    }

    @Override
    public void validate(final ValidationHandler aHandler) {
        new CategoryValidator(aHandler, this).validate();
    }

    public Category deactivate() {
        if (getDeletedAt() == null) {
            this.deletedAt = Instant.now();
        }
        this.active = false;
        this.updatedAt = Instant.now();
        return this;
    }

    public Category activate() {
        this.active = true;
        this.updatedAt = Instant.now();
        this.deletedAt = null;
        return this;
    }

    public Category update(
            final String aName, final String aDescription, final boolean isActivate
    ) {
        this.name = aName;
        this.description = aDescription;
        if (isActivate) {
            this.activate();
        } else {
            this.deactivate();
        }
        this.updatedAt = Instant.now();
        return this;
    }

    private Category(
            final CategoryId aId,
            final String aName,
            final String aDescription,
            final boolean isActive,
            final Instant aCreatedDate,
            final Instant aUpdatedDate,
            final Instant aDeletedDate
    ) {
        super(aId);
        this.name = aName;
        this.description = aDescription;
        this.active = isActive;
        this.createdAt = aCreatedDate;
        this.updatedAt = aUpdatedDate;
        this.deletedAt = aDeletedDate;
    }

    public CategoryId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }


    @Override
    public Category clone() {
        try {
            Category clone = (Category) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
