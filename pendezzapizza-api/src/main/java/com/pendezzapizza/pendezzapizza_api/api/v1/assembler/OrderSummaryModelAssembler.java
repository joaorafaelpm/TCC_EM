package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.OrderSummaryMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.OrderSummaryModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.Order;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Assembler da minha entidade de <b>resumo de pedido</b>
 *
 * <p>Essa é uma classe auxiliar que serve para usar o mapper de forma indireta</p>
 * <p>Opto por não usar o mapper direto para abrir a possibilidade de implementação de links (a gosto do freguês). Ou simplesmente adicionar lógica aqui dentro caso seja necessário </p>
 */
@Component
@AllArgsConstructor
public class OrderSummaryModelAssembler {

    private OrderSummaryMapper orderSummaryMapper;

    public OrderSummaryModel toModel(Order order) {
        return orderSummaryMapper.toModel(order);
    }

    public Collection<OrderSummaryModel> toCollectionModel(Collection<Order> entities) {
        return entities.stream().map((this::toModel)).toList();
    }
}