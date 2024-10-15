package com.juliomesquita.admin.catalog.infrastructure.api.controllers;

import com.juliomesquita.admin.catalog.domain.commom.exceptions.DomainException;
import com.juliomesquita.admin.catalog.domain.commom.validation.Error;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = DomainException.class)
    public ResponseEntity<?> handleDomainException(final DomainException exception) {
        return ResponseEntity.unprocessableEntity()
                .body(ApiError.from(exception));
    }

    private record ApiError(String message, List<Error> errors) {
        public static ApiError from(final DomainException ex) {
            return new ApiError(ex.getMessage(), ex.getErrors());
        }
    }
}
