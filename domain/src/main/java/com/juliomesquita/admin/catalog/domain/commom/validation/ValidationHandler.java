package com.juliomesquita.admin.catalog.domain.commom.validation;

import java.util.List;

public interface ValidationHandler {
    ValidationHandler append(Error anError);

    ValidationHandler append(ValidationHandler anHandler);

    <T> T validate(Validation<T> anValidation);

    List<Error> getErrors();

    default boolean booleanHasError() {
        return getErrors() != null && !getErrors().isEmpty();
    }

    default Error firstError() {
        if (getErrors() != null && !getErrors().isEmpty()) {
            return getErrors().get(0);
        }
        return null;

    }

    interface Validation<T> {
        T validate();
    }
}
