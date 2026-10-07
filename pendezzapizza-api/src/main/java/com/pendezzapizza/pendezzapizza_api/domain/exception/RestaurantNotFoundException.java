package com.pendezzapizza.pendezzapizza_api.domain.exception;

import java.io.Serial;
import java.util.UUID;

/**
 * Excessão específica para entidade <b>restaurante</b> não encontrada
 */
public class RestaurantNotFoundException extends EntityNotFoundException {

    @Serial
    private static final long serialVersionUID = 1L;

    public RestaurantNotFoundException(String message) {
        super(message);
    }
    public RestaurantNotFoundException(UUID id) {
        super(String.format("Restaurante de id '%s' não encontrado!" , id));
    }
}
