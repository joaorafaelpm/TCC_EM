package com.pendezzapizza.pendezzapizza_api.domain.service;


import com.pendezzapizza.pendezzapizza_api.domain.model.OrderItem;
import com.pendezzapizza.pendezzapizza_api.domain.repository.OrderItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    public List<OrderItem> findAll () {
        return orderItemRepository.findAll();
    }
    public OrderItem findById (UUID id) {
        return orderItemRepository.findByIdOrThrowException(id);
    }


}
