package com.pendezzapizza.pendezzapizza_api.domain.model.enuns;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * Enumeração criada para controlar o status do pedido
 *
 * <p>Recebe uma descrição e o status anterior para controle de fase</p>
 */
public enum OrderStatus {

    CREATED("Created"),
    CONFIRMED("Confirmed", CREATED),
    DELIVERED("Delivered", CONFIRMED),
    CANCELED("Canceled", CREATED);

    @Getter
    private final String description;

    private final List<OrderStatus> previousStatuses;

    OrderStatus(String description, OrderStatus... previousStatuses) {
        this.description = description;
        this.previousStatuses = Arrays.asList(previousStatuses);
    }

    public boolean cannotChangeTo(OrderStatus newStatus) {
        return !newStatus.previousStatuses.contains(this);
    }

    public boolean canChangeTo(OrderStatus newStatus) {
        return !cannotChangeTo(newStatus);
    }
}
