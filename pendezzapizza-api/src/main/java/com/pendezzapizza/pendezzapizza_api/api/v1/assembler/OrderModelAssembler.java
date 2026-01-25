package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.PendezzaLinks;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.OrderMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.controller.OrderController;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.OrderModel;
import com.pendezzapizza.pendezzapizza_api.core.security.PendezzaPizzaSecurity;
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
    private PendezzaLinks pendezzaLinks;

    @Autowired
    private PendezzaPizzaSecurity pendezzaPizzaSecurity;

    public OrderModelAssembler() {
        super(OrderController.class, OrderModel.class);
    }

    @Override
    public OrderModel toModel(Order order) {
        OrderModel orderModel = orderMapper.toModel(order);

        UUID orderId = orderModel.getId();
        UUID restaurantId = orderModel.getRestaurant().getId();
        UUID clientId = orderModel.getCustomer().getId();
        UUID cityId = orderModel.getDeliveryAddress().getCity().getId();
        UUID paymentMethodId = orderModel.getPaymentMethod().getId();

        // Ações de gerenciamento de estado do pedido
        if (pendezzaPizzaSecurity.canManageOrders(order.getId())) {
            if (order.canBeConfirmed()) {
                orderModel.add(pendezzaLinks.linkToConfirmOrder(orderId, "confirm"));
            }
            if (order.canBeDelivered()) {
                orderModel.add(pendezzaLinks.linkToDeliverOrder(orderId, "deliver"));
            }
            if (order.canBeCanceled()) {
                orderModel.add(pendezzaLinks.linkToCancelOrder(orderId, "cancel"));
            }
        }

        if (pendezzaPizzaSecurity.canSearchOrders()) {
            orderModel.add(pendezzaLinks.linkToOrders("orders"));
        }

        if (pendezzaPizzaSecurity.canConsultRestaurants()) {
            orderModel.getRestaurant().add(pendezzaLinks.linkToRestaurant(restaurantId));

            // Links para produtos dentro dos itens do pedido
            orderModel.getItems().forEach(item -> {
                item.add(pendezzaLinks.linkToProduct(
                        orderModel.getRestaurant().getId(), item.getProductId(), IanaLinkRelations.SELF.value()));
            });
        }

        if (pendezzaPizzaSecurity.canConsultCities()) {
            orderModel.getDeliveryAddress().getCity().add(pendezzaLinks.linkToCity(cityId));
        }

        if (pendezzaPizzaSecurity.canConsultPaymentMethods()) {
            orderModel.getPaymentMethod().add(pendezzaLinks.linkToPaymentMethod(paymentMethodId));
        }

        if (pendezzaPizzaSecurity.canConsultUsersGroupsPermissions()) {
            orderModel.getCustomer().add(pendezzaLinks.linkToUser(clientId));
        }

        return orderModel;
    }
}