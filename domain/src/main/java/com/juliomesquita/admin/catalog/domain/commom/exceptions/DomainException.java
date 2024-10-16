package com.juliomesquita.admin.catalog.domain.commom.exceptions;

import com.juliomesquita.admin.catalog.domain.commom.validation.Error;

import java.util.List;

public class DomainException extends NoStackTraceException {
    private final List<Error> errors;

    public static DomainException with(final Error aError) {
        return new DomainException("", List.of(aError));
    }

    public static DomainException with(final List<Error> aErrors) {
        return new DomainException("", aErrors);
    }

    protected DomainException(final String aMassege, final List<Error> aErrors) {
        super(aMassege);
        this.errors = aErrors;
    }

    public List<Error> getErrors() {
        return errors;
    }
}
