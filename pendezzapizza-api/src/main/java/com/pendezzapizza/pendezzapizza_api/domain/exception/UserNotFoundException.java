package com.pendezzapizza.pendezzapizza_api.domain.exception;

import java.io.Serial;
import java.util.UUID;

/**
 * Excessão específica para entidade <b>usuário</b> não encontrada
 */
public class UserNotFoundException extends EntityNotFoundException{

    @Serial
    private static final long serialVersionUID = 1L;

    public UserNotFoundException(String message) {
        super(message);
    }
    public UserNotFoundException(UUID id) {
        super(String.format("User com ID %s não foi encontrado." , id));
    }
}
