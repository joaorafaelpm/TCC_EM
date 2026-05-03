package com.pendezzapizza.pendezzapizza_api.domain.listener;


import com.pendezzapizza.pendezzapizza_api.domain.event.ConfirmationOrderEvent;
import com.pendezzapizza.pendezzapizza_api.domain.model.Order;
import com.pendezzapizza.pendezzapizza_api.domain.service.SendEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Listener de evento de <b>confirmação de pedido</b>
 *
 * <p>Essa classe executa todo evento relacionado a minha classe de evento {@code ConfirmationOrderEvent}</p>
 */
@Component
public class NotificationCustomerOrderConfirmed {

//    Serviço de envio de email
    @Autowired
    private SendEmailService sendEmailService;

//    Quando o pedido é confirmado envia o email
    @TransactionalEventListener
    private void whenConfirmedOrder (ConfirmationOrderEvent event) {
//        Recebe o pedido pelo evento e escreve o email
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
