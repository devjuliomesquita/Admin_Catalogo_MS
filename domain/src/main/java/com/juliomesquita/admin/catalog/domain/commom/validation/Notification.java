package com.juliomesquita.admin.catalog.domain.commom.validation;

import com.juliomesquita.admin.catalog.domain.commom.exceptions.DomainException;

import java.util.ArrayList;
import java.util.List;

public class Notification implements ValidationHandler {
    private final List<Error> errors;

    public static Notification create() {
        return new Notification(new ArrayList<>());
    }

    public static Notification create(final Throwable t) {
        return create(new Error(t.getMessage()));
    }

    public static Notification create(final Error anError) {
        return new Notification(new ArrayList<>()).append(anError);
    }

    @Override
    public Notification append(Error anError) {
        this.errors.add(anError);
        return this;
    }

    @Override
    public Notification append(ValidationHandler anHandler) {
        this.errors.addAll(anHandler.getErrors());
        return null;
    }

    @Override
    public <T> T validate(Validation<T> anValidation) {
        try {
            return anValidation.validate();
        } catch (final DomainException ex) {
            this.errors.addAll(ex.getErrors());
        } catch (final Throwable ex) {
            this.errors.add(new Error(ex.getMessage()));
        }
        return null;
    }

    @Override
    public List<Error> getErrors() {
        return this.errors;
    }

    private Notification(final List<Error> errors) {
        this.errors = errors;
    }
}
