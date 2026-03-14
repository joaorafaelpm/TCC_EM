package com.pendezzapizza.pendezzapizza_api.domain.service;

import com.pendezzapizza.pendezzapizza_api.domain.model.Order;
import com.pendezzapizza.pendezzapizza_api.domain.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderFlowService {

    private final OrderService orderService;

    private final OrderRepository orderRepository;

    @Transactional
    public void confirm(UUID orderId) {
        Order order = orderService.findById(orderId);
        order.confirm();

        orderRepository.save(order);
    }

    @Transactional
    public void deliver(UUID orderId) {
        Order order = orderService.findById(orderId);
        order.deliver();
    }

    @Transactional
    public void cancel(UUID id) {
        Order order = orderService.findById(id);
        order.cancel();

        orderRepository.save(order);
    }
}
