package com.pendezzapizza.pendezzapizza_api.domain.exception;

import java.io.Serial;

public class InvalidEntityException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidEntityException(String message) {
        super(message);
    }
}
