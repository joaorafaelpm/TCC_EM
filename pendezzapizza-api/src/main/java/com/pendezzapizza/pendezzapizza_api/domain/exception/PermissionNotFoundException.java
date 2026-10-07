package com.pendezzapizza.pendezzapizza_api.domain.exception;

import java.io.Serial;
import java.util.UUID;

/**
 * Excessão específica para entidade <b>permissão</b> não encontrada
 */
public class PermissionNotFoundException extends EntityNotFoundException {

    @Serial
    private static final long serialVersionUID = 1L;

    public PermissionNotFoundException(String message) {
        super(message);
    }
    public PermissionNotFoundException(UUID id) {
        super(String.format("Permissao de id '%s' não encontrado!" , id));
    }
}