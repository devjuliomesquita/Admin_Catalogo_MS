package com.juliomesquita.admin.catalog.domain.category;

import com.juliomesquita.admin.catalog.domain.commom.abstractions.Identifier;

import java.util.Objects;
import java.util.UUID;

public class CategoryId extends Identifier {
    private final String value;

    public static CategoryId unique() {
        return from(UUID.randomUUID());
    }

    public static CategoryId from(final String aId) {
        return new CategoryId(aId);
    }

    public static CategoryId from(final UUID aId) {
        return new CategoryId(aId.toString().toLowerCase());
    }

    private CategoryId(final String value) {
        Objects.requireNonNull(value, "'value' should not be null");
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        final CategoryId that = (CategoryId) object;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getValue());
    }
}
