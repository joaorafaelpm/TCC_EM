package com.pendezzapizza.pendezzapizza_api.domain.event;

import com.pendezzapizza.pendezzapizza_api.domain.model.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Evento de confirmação de pedido
 *
 * <p>Recebe um objeto da classe {@code Order} para confirma-lo</p>
 */
@Getter
@AllArgsConstructor
public class ConfirmationOrderEvent {

    private Order order;

}
