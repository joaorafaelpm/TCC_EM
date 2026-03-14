package com.pendezzapizza.pendezzapizza_api.domain.exception;

import java.io.Serial;
import java.util.UUID;

public class StateNotFoundException extends EntityNotFoundException {

    @Serial
    private static final long serialVersionUID = 1L;

    public StateNotFoundException(String message) {
        super(message);
    }
    public StateNotFoundException(UUID id) {
        super(String.format("Estado de id '%s' não encontrado!" , id));
    }
}
