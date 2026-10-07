package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.OrderMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.OrderModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.Order;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Component;

/**
 * Assembler da minha entidade de <b>pedido</b>
 *
 * <p>Essa é uma classe auxiliar que serve para usar o mapper de forma indireta</p>
 * <p>Opto por não usar o mapper direto para abrir a possibilidade de implementação de links (a gosto do freguês). Ou simplesmente adicionar lógica aqui dentro caso seja necessário </p>
 */
@Component
@AllArgsConstructor
public class OrderModelAssembler {

    private OrderMapper orderMapper;

    public OrderModel toModel(Order order) {
        return orderMapper.toModel(order);
    }
}