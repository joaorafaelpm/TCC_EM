package com.pendezzapizza.pendezzapizza_api.domain.event;

import com.pendezzapizza.pendezzapizza_api.domain.model.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Evento de cancelamento de pedido
 *
 * <p>Recebe um objeto da classe {@code Order} para cancela-los</p>
 */
@Getter
@AllArgsConstructor
public class OrderCancellationEvent {

    private Order order;

}
