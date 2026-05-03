package com.pendezzapizza.pendezzapizza_api.domain.exception;

import java.io.Serial;
import java.util.UUID;

/**
 * Excessão específica para entidade <b>produto</b> não encontrada
 */
public class ProductNotFoundException extends EntityNotFoundException {

    @Serial
    private static final long serialVersionUID = 1L;

    public ProductNotFoundException(String message) {
        super(message);
    }
    public ProductNotFoundException(UUID restauranteId, UUID produtoId) {
        this(String.format("Não existe um cadastro de produto com id '%s' para o restaurante de id '%s'",
                produtoId, restauranteId));
    }
    public ProductNotFoundException(UUID produtoId) {
        this(String.format("Não existe um cadastro de produto com id '%s'",
                produtoId));
    }
}
