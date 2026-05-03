package com.pendezzapizza.pendezzapizza_api.domain.exception;

import java.io.Serial;
import java.util.UUID;

/**
 * Excessão específica para entidade <b>cidade</b> não encontrada
 */
public class CityNotFoundException extends EntityNotFoundException {

    @Serial
    private static final long serialVersionUID = 1L;


    public CityNotFoundException(String message) {
        super(message);
    }
    public CityNotFoundException(String cityName , String stateName) {
        super(String.format("Cidade com nome '%s' não encontrada dentro do estado '%s'!" , cityName , stateName));
    }
    public CityNotFoundException(UUID id) {
        super(String.format("Cidade com id '%s' não encontrada!" , id));
    }
}
