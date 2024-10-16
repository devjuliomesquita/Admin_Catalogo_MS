package com.juliomesquita.admin.catalog.domain.commom.exceptions;

import com.juliomesquita.admin.catalog.domain.commom.abstractions.AggregateRoot;
import com.juliomesquita.admin.catalog.domain.commom.abstractions.Identifier;
import com.juliomesquita.admin.catalog.domain.commom.validation.Error;

import java.util.Collections;
import java.util.List;

public class NotFoundException extends DomainException {

    protected NotFoundException(String aMassege, List<Error> aErrors) {
        super(aMassege, aErrors);
    }

    public static NotFoundException with(
            final Class<? extends AggregateRoot<?>> anAggregate,
            final Identifier anId
    ) {
        final String anError =
                "%s with ID %s was not found".formatted(
                        anAggregate.getSimpleName(),
                        anId.getValue());
        return new NotFoundException(anError, Collections.emptyList());
    }
}
