package com.pendezzapizza.pendezzapizza_api.domain.exception;

import java.io.Serial;
import java.util.UUID;

public class GroupNotFoundException extends EntityNotFoundException {

    @Serial
    private static final long serialVersionUID = 1L;

    public GroupNotFoundException(String message) {
        super(message);
    }
    public GroupNotFoundException(UUID id) {
        super(String.format("Grupo de id '%s' não encontrado!" , id));
    }
}
