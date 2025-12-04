package com.pendezzapizza.pendezzapizza_api.domain.exception;


import java.io.Serial;
import java.util.UUID;

public class EntityInUseException extends BusinessException {

    @Serial
    private static final long serialVersionUID = 1L;

    public EntityInUseException(String message) {
        super(message);
    }
    public EntityInUseException(Long id) {
        super(String.format(
                "Entidade com id '%s' já está sendo usada!" , id
        ));
    }
    public EntityInUseException(UUID id) {
        super(String.format(
                "Entidade com id '%s' já está sendo usada!" , id
        ));
    }

}
