package com.araguacaima.gsa.persistence.commons.exceptions;

public class EntityError extends Error {

    public EntityError(String message) {
        super(message);
    }

    public EntityError(String message, Throwable cause) {
        super(message, cause);
    }
}
