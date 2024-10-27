package com.juliomesquita.admin.catalog.domain.genre;

import com.juliomesquita.admin.catalog.domain.commom.abstractions.Identifier;

import java.util.Objects;
import java.util.UUID;

public class GenreId extends Identifier {
    private final String value;

    public static GenreId unique() {
        return from(UUID.randomUUID());
    }

    public static GenreId from(final String anId) {
        return new GenreId(anId);
    }

    public static GenreId from(final UUID anId) {
        return new GenreId(anId.toString());
    }


    private GenreId(final String value) {
        this.value = Objects.requireNonNull(value, "'value' should not be null");
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        final GenreId genreId = (GenreId) object;
        return Objects.equals(getValue(), genreId.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getValue());
    }
}
