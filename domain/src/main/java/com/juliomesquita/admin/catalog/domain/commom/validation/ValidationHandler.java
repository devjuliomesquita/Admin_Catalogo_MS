package com.juliomesquita.admin.catalog.domain.commom.validation;

import java.util.List;

public interface ValidationHandler {
    ValidationHandler append(Error  anError);
    ValidationHandler append(ValidationHandler  anHandler);
    ValidationHandler validate(Validation  anValidation);

    List<Error> getErrors();

    default boolean booleanHasError(){
        return getErrors() != null && !getErrors().isEmpty();
    }

    interface Validation {
        void validate();
    }
}
