package com.pendezzapizza.pendezzapizza_api.domain.exception;

import java.io.Serial;

/**
 * Excessão específica para entidade não encontrada
 */
public class EntityNotFoundException extends BusinessException {

    @Serial
    private static final long serialVersionUID = 1L;

    public EntityNotFoundException(String message) {
        super(message);
    }

}
