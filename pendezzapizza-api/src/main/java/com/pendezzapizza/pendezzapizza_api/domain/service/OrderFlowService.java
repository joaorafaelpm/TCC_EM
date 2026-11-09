package com.pendezzapizza.pendezzapizza_api.domain.service;

import com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.action.OrdersActionCacheEvict;
import com.pendezzapizza.pendezzapizza_api.domain.model.Order;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderFlowService {

    private final OrderService orderService;
    private final StockService stockService;
    
    @OrdersActionCacheEvict
    @Transactional
    public void confirm(UUID orderId) {
        Order order = orderService.findById(orderId);
        order.confirm();

        orderService.save(order);
    }

    @OrdersActionCacheEvict
    @Transactional
    public void deliver(UUID orderId) {
        Order order = orderService.findById(orderId);
        order.deliver();
        orderService.save(order);
    }

    // @OrdersActionCacheEvict
    // @Transactional
    // public void cancel(UUID orderId) {
    //     Order order = orderService.findById(orderId);
    //     order.cancel();

    //     orderService.save(order);
    // }
    // private final StockService stockService;

    @OrdersActionCacheEvict
    @Transactional
    public void cancel(UUID orderId) {
        Order order = orderService.findById(orderId);
        order.cancel();
        orderService.save(order);
        stockService.restoreForOrder(order);
    }
}
