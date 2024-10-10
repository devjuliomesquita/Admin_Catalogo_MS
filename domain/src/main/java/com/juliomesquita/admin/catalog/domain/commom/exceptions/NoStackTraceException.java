package com.juliomesquita.admin.catalog.domain.commom.exceptions;

public class NoStackTraceException extends RuntimeException {
    public NoStackTraceException(final String message) {
        super(message, null);
    }

    public NoStackTraceException(final String message, final Throwable cause) {
        super(message, cause, true, false);
    }
}
