package com.pendezzapizza.pendezzapizza_api.domain.exception;

import java.io.Serial;
import java.util.UUID;

/**
 * Excessão específica para entidade <b>pedido</b> não encontrada
 */
public class OrderNotFoundException extends EntityNotFoundException {

    @Serial
    private static final long serialVersionUID = 1L;

    public OrderNotFoundException(String codigo) {
        super(String.format("Pedido de codigo '%s' não encontrado!" , codigo));
    }
    public OrderNotFoundException(UUID id) {
        super(String.format("Pedido de id '%s' não encontrado!" , id));
    }
}
