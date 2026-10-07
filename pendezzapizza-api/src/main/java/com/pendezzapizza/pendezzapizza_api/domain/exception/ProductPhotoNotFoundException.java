package com.pendezzapizza.pendezzapizza_api.domain.exception;

import java.io.Serial;
import java.util.UUID;

/**
 * Excessão específica para entidade <b>foto de produto</b> não encontrada
 */
public class ProductPhotoNotFoundException extends EntityNotFoundException {

    @Serial
    private static final long serialVersionUID = 1L;

    public ProductPhotoNotFoundException(String message) {
        super(message);
    }
    public ProductPhotoNotFoundException(UUID restauranteId, UUID produtoId) {
        this(String.format("Foto do produto de id '%s' não encontrado no restaurante de id '%s' ",
                produtoId, restauranteId));
    }
}
