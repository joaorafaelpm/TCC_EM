package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.OrderMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.OrderModel;
import com.pendezzapizza.pendezzapizza_api.core.security.PendezzaPizzaSecurity;
import com.pendezzapizza.pendezzapizza_api.domain.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderModelAssembler {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private PendezzaPizzaSecurity pendezzaPizzaSecurity;

    public OrderModel toModel(Order order) {
        return orderMapper.toModel(order);
    }
}