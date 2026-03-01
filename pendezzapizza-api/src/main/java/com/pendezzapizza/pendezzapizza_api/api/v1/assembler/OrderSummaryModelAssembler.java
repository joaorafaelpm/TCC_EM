package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.OrderSummaryMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.OrderSummaryModel;
import com.pendezzapizza.pendezzapizza_api.core.security.PendezzaPizzaSecurity;
import com.pendezzapizza.pendezzapizza_api.domain.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class OrderSummaryModelAssembler {

    @Autowired
    private OrderSummaryMapper orderSummaryMapper;


    @Autowired
    private PendezzaPizzaSecurity pendezzaPizzaSecurity;


    public OrderSummaryModel toModel(Order order) {
        return orderSummaryMapper.toModel(order);
    }

    public Collection<OrderSummaryModel> toCollectionModel(Collection<Order> entities) {
        return entities.stream().map((this::toModel)).toList();
    }
}