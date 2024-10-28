package com.juliomesquita.admin.catalog.domain.genre;

import com.juliomesquita.admin.catalog.domain.category.CategoryId;
import com.juliomesquita.admin.catalog.domain.commom.abstractions.AggregateRoot;
import com.juliomesquita.admin.catalog.domain.commom.exceptions.NotificationException;
import com.juliomesquita.admin.catalog.domain.commom.utils.InstantUtil;
import com.juliomesquita.admin.catalog.domain.commom.validation.Notification;
import com.juliomesquita.admin.catalog.domain.commom.validation.ValidationHandler;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Genre extends AggregateRoot<GenreId> {
    private String name;
    private boolean active;
    private List<CategoryId> categories;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    public static Genre with(final Genre aGenre) {
        return with(
                aGenre.getId(),
                aGenre.getName(),
                aGenre.isActive(),
                aGenre.getCategories(),
                aGenre.getCreatedAt(),
                aGenre.getUpdatedAt(),
                aGenre.getDeletedAt()
        );
    }

    public static Genre with(
            final GenreId anId,
            final String aName,
            final boolean active,
            final List<CategoryId> aCategories,
            final Instant aCreatedAt,
            final Instant anUpdatedAt,
            final Instant aDeletedAt
    ) {
        return new Genre(anId, aName, active, aCategories, aCreatedAt, anUpdatedAt, aDeletedAt);
    }

    public static Genre newGenre(final String name, final boolean active) {
        final GenreId anId = GenreId.unique();
        final Instant now = InstantUtil.now();
        final Instant deletedAt = active ? null : now;
        return new Genre(anId, name, active, new ArrayList<>(), now, now, deletedAt);
    }

    public Genre update(
            final String aName,
            final boolean active,
            final List<CategoryId> categories
    ) {
        if (active) {
            this.activate();
        } else {
            this.deactivate();
        }

        this.name = aName;
        this.updatedAt = InstantUtil.now();
        this.categories = new ArrayList<>(categories != null ? categories : Collections.emptyList());
        this.selfValidate();

        return this;
    }

    public Genre deactivate() {
        if (deletedAt == null) {
            this.deletedAt = InstantUtil.now();
        }
        this.active = false;
        this.updatedAt = InstantUtil.now();
        return this;
    }

    public Genre activate() {
        this.active = true;
        this.updatedAt = InstantUtil.now();
        this.deletedAt = null;
        return this;
    }

    public Genre addCategory(final CategoryId aCategoryId) {
        if (aCategoryId == null) {
            return this;
        }

        this.categories.add(aCategoryId);
        this.updatedAt = InstantUtil.now();
        return this;
    }

    public Genre addCategories(final List<CategoryId> aCategories) {
        if (aCategories == null || aCategories.isEmpty()) {
            return this;
        }

        this.categories.addAll(aCategories);
        this.updatedAt = InstantUtil.now();
        return this;
    }

    public Genre removeCategory(final CategoryId aCategoryId){
        if (aCategoryId == null) {
            return this;
        }

        this.categories.remove(aCategoryId);
        this.updatedAt = InstantUtil.now();
        return this;
    }

    private void selfValidate() {
        final Notification notification = Notification.create();
        validate(notification);
        if (notification.booleanHasError()) {
            throw new NotificationException("Failed to create a Aggregate Genre.", notification);
        }
    }

    @Override
    public void validate(final ValidationHandler aHandler) {
        new GenreValidator(aHandler, this).validate();
    }

    private Genre(
            final GenreId genreId,
            final String aName,
            final boolean active,
            final List<CategoryId> aCategories,
            final Instant aCreatedAt,
            final Instant anUpdatedAt,
            final Instant aDeletedAt
    ) {
        super(genreId);
        this.name = aName;
        this.active = active;
        this.categories = aCategories;
        this.createdAt = aCreatedAt;
        this.updatedAt = anUpdatedAt;
        this.deletedAt = aDeletedAt;
        this.selfValidate();
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public List<CategoryId> getCategories() {
        return Collections.unmodifiableList(categories);
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
}
