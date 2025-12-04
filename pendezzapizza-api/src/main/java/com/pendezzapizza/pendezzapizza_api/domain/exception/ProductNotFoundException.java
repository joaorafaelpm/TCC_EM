package com.pendezzapizza.pendezzapizza_api.domain.exception;

import java.io.Serial;
import java.util.UUID;

public class ProductNotFoundException extends EntityNotFoundException {

    @Serial
    private static final long serialVersionUID = 1L;

    public ProductNotFoundException(String message) {
        super(message);
    }
    public ProductNotFoundException(UUID restauranteId, UUID produtoId) {
        this(String.format("Não existe um cadastro de produto com código '%s' para o restaurante de código '%s'",
                produtoId, restauranteId));
    }
}
