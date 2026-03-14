package com.pendezzapizza.pendezzapizza_api.domain.exception;

import java.io.Serial;
import java.util.UUID;

public class OrderItemNotFoundException extends EntityNotFoundException {

    @Serial
    private static final long serialVersionUID = 1L;

    public OrderItemNotFoundException(String message) {
        super(message);
    }
    public OrderItemNotFoundException(UUID id) {
        super(String.format("Item do pedido de id '%s' não encontrado!" , id));
    }
}
