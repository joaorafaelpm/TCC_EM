package com.pendezzapizza.pendezzapizza_api.domain.listener;

import com.pendezzapizza.pendezzapizza_api.domain.event.OrderCancellationEvent;
import com.pendezzapizza.pendezzapizza_api.domain.model.Order;
import com.pendezzapizza.pendezzapizza_api.domain.service.SendEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class NotificationCanceledOrderEmail {

    @Autowired
    private SendEmailService sendEmailService;

    @TransactionalEventListener
    private void whenCancelOrder (OrderCancellationEvent event) {

        Order order = event.getOrder();
        var message = SendEmailService.Message.builder()
                .subject(order.getRestaurant().getName() + " - Pedido Cancelado")
                .body("canceled-order.html")
                .variable("order" , order)
                .recipient(order.getCustomer().getEmail())
                .build();
        sendEmailService.send(message);
    }

}
