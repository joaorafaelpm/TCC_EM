package com.pendezzapizza.pendezzapizza_api.domain.exception;

import java.io.Serial;

public class EntityNotFoundException extends BusinessException {

    @Serial
    private static final long serialVersionUID = 1L;

    public EntityNotFoundException(String message) {
        super(message);
    }

}
