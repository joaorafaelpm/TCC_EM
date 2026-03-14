package com.pendezzapizza.pendezzapizza_api.domain.listener;


import com.pendezzapizza.pendezzapizza_api.domain.event.ConfirmationOrderEvent;
import com.pendezzapizza.pendezzapizza_api.domain.model.Order;
import com.pendezzapizza.pendezzapizza_api.domain.service.SendEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class NotificationCustomerOrderConfirmed {

    @Autowired
    private SendEmailService sendEmailService;


    @TransactionalEventListener
    private void whenConfirmedOrder (ConfirmationOrderEvent event) {

        Order order = event.getOrder();
        var message = SendEmailService.Message.builder()
                .subject(order.getRestaurant().getName() + " - Pedido Confirmado")
                .body("confirmed-order.html")
                .variable("order" , order)
                .recipient(order.getCustomer().getEmail())
                .build();
        sendEmailService.send(message);
    }

}
