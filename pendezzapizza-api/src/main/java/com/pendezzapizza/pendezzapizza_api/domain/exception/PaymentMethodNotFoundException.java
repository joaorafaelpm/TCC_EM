package com.pendezzapizza.pendezzapizza_api.domain.exception;

import java.io.Serial;
import java.util.UUID;

public class PaymentMethodNotFoundException extends EntityNotFoundException {

    @Serial
    private static final long serialVersionUID = 1L;

    public PaymentMethodNotFoundException(String message) {
        super(message);
    }
    public PaymentMethodNotFoundException(UUID id) {
        super(String.format("Forma de pagamento de id '%s' não encontrada!" , id));
    }
}
