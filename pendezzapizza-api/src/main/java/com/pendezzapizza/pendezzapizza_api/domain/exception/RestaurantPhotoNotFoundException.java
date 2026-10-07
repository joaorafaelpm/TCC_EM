package com.pendezzapizza.pendezzapizza_api.domain.exception;

import java.io.Serial;
import java.util.UUID;

/**
 * Excessão específica para entidade <b>foto de restaurante</b> não encontrada
 */
public class RestaurantPhotoNotFoundException extends EntityNotFoundException {

    @Serial
    private static final long serialVersionUID = 1L;

    public RestaurantPhotoNotFoundException(String message) {
        super(message);
    }
    public RestaurantPhotoNotFoundException(UUID restauranteId) {
        this(String.format("Foto do restaurante de id '%s' não encontrado",
               restauranteId));
    }
}
