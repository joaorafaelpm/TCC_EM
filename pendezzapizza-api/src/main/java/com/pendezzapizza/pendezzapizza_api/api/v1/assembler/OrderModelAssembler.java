package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.PendezzaPizzaLinks;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.OrderMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.controller.OrderController;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.OrderModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderModelAssembler extends RepresentationModelAssemblerSupport<Order, OrderModel> {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private PendezzaPizzaLinks links;

    public OrderModelAssembler() {
        super(OrderController.class, OrderModel.class);
    }

    @Override
    public OrderModel toModel(Order order) {
        OrderModel model = orderMapper.toModel(order);

        UUID orderId = model.getId();
        UUID restaurantId = model.getRestaurant().getId();
        UUID customerId = model.getClient().getId();
        UUID cityId = model.getDeliveryAddress().getCity().getId();
        UUID paymentMethodId = model.getPaymentMethod().getId();

        if (order.canBeConfirmed()) {
            model.add(links.linkToConfirmOrder(orderId, "confirm"));
        }
        if (order.canBeDelivered()) {
            model.add(links.linkToDeliverOrder(orderId, "deliver"));
        }
        if (order.canBeCanceled()) {
            model.add(links.linkToCancelOrder(orderId, "cancel"));
        }

        model.add(links.linkToOrders("orders"));
        model.getRestaurant().add(links.linkToRestaurant(restaurantId));
        model.getDeliveryAddress().getCity().add(links.linkToCity(cityId));
        model.getPaymentMethod().add(links.linkToPaymentMethod(paymentMethodId));
        model.getClient().add(links.linkToUser(customerId));

        model.getItems().forEach(item ->
                item.add(links.linkToProduct(
                        restaurantId, item.getProductId(), IanaLinkRelations.SELF.value()))
        );

        return model;
    }
}
