package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.PendezzaLinks;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.OrderSummaryMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.controller.OrderController;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.OrderSummaryModel;
import com.pendezzapizza.pendezzapizza_api.core.security.PendezzaPizzaSecurity;
import com.pendezzapizza.pendezzapizza_api.domain.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderSummaryModelAssembler extends RepresentationModelAssemblerSupport<Order, OrderSummaryModel> {

    @Autowired
    private OrderSummaryMapper orderSummaryMapper;

    @Autowired
    private PendezzaLinks pendezzaLinks;

    @Autowired
    private PendezzaPizzaSecurity pendezzaPizzaSecurity;

    public OrderSummaryModelAssembler() {
        super(OrderController.class, OrderSummaryModel.class);
    }

    @Override
    public OrderSummaryModel toModel(Order order) {
        OrderSummaryModel orderModel = orderSummaryMapper.toModel(order);

        UUID restaurantId = orderModel.getRestaurant().getId();
        UUID customerId = order.getCustomer().getId();

        if (pendezzaPizzaSecurity.canSearchOrders()) {
            orderModel.add(pendezzaLinks.linkToOrders(IanaLinkRelations.COLLECTION.value()));
            orderModel.add(pendezzaLinks.linkToOrder(orderModel.getId()));
        }

        if (pendezzaPizzaSecurity.canConsultRestaurants()) {
            orderModel.getRestaurant().add(pendezzaLinks.linkToRestaurant(restaurantId));
        }

        if (pendezzaPizzaSecurity.canConsultUsersGroupsPermissions()) {
            orderModel.getCustomer().add(pendezzaLinks.linkToUser(customerId));
        }

        return orderModel;
    }

    @Override
    public CollectionModel<OrderSummaryModel> toCollectionModel(Iterable<? extends Order> entities) {
        CollectionModel<OrderSummaryModel> ordersCollectionModel = super.toCollectionModel(entities);

        if (pendezzaPizzaSecurity.canSearchOrders()) {
            ordersCollectionModel.add(pendezzaLinks.linkToOrders("orders"));
        }

        return ordersCollectionModel;
    }
}