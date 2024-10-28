package com.juliomesquita.admin.catalog.domain.commom.validation;

import com.juliomesquita.admin.catalog.domain.commom.exceptions.DomainException;

import java.util.List;

public class ThrowsValidationHandler implements ValidationHandler {
    @Override
    public ValidationHandler append(final Error anError) {
        throw DomainException.with(anError);
    }

    @Override
    public ValidationHandler append(final ValidationHandler anHandler) {
        throw DomainException.with(anHandler.getErrors());
    }

    @Override
    public <T> T validate(final Validation<T> anValidation) {
        try {
            return anValidation.validate();
        } catch (final Exception ex){
            throw DomainException.with(new Error(ex.getMessage()));
        }
    }

    @Override
    public List<Error> getErrors() {
        return List.of();
    }
}
