package com.pendezzapizza.pendezzapizza_api.domain.event;

import com.pendezzapizza.pendezzapizza_api.domain.model.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderCancellationEvent {

    private Order order;

}
