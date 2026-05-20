package com.pendezzapizza.pendezzapizza_api.domain.exception;

import java.io.Serial;
import java.util.UUID;

/**
 * Excessão específica para entidade <b>grupo</b> não encontrada
 */
public class GroupNotFoundException extends EntityNotFoundException {

    @Serial
    private static final long serialVersionUID = 1L;

    public GroupNotFoundException(UUID id) {
        super(String.format("Grupo de id '%s' não encontrado!" , id));
    }
    public GroupNotFoundException(String nome) {
        super(String.format("Grupo de nome '%s' não encontrado!" , nome));
    }
}
