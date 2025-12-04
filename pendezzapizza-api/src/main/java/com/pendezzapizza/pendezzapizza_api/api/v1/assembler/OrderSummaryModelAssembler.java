package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.PendezzaPizzaLinks;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.OrderSummaryMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.controller.OrderController;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.OrderSummaryModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class OrderSummaryModelAssembler extends RepresentationModelAssemblerSupport<Order, OrderSummaryModel> {

    @Autowired
    private OrderSummaryMapper orderSummaryMapper;

    @Autowired
    private PendezzaPizzaLinks appLinks;

    public OrderSummaryModelAssembler() {
        super(OrderController.class, OrderSummaryModel.class);
    }

    @Override
    public OrderSummaryModel toModel(Order order) {
        OrderSummaryModel model = orderSummaryMapper.toModel(order);

        model.add(appLinks.linkToOrders(IanaLinkRelations.COLLECTION.value()));

        model.getRestaurant().add(appLinks.linkToRestaurant(model.getRestaurant().getId()));
        model.getClient().add(appLinks.linkToUser(model.getClient().getId()));

        model.add(appLinks.linkToOrder(model.getId()));

        return model;
    }

    public CollectionModel<OrderSummaryModel> toCollection(Collection<Order> orders) {
        var models = orders.stream().map(this::toModel).toList();
        CollectionModel<OrderSummaryModel> collection = CollectionModel.of(models);
        collection.add(appLinks.linkToOrders("orders"));
        return collection;
    }
}
