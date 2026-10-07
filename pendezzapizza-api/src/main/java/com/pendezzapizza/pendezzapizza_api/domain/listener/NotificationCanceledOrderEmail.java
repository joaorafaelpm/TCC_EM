package com.pendezzapizza.pendezzapizza_api.domain.listener;

import com.pendezzapizza.pendezzapizza_api.domain.event.OrderCancellationEvent;
import com.pendezzapizza.pendezzapizza_api.domain.model.Order;
import com.pendezzapizza.pendezzapizza_api.domain.service.SendEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Listener de evento de <b>cancelamento de pedido</b>
 *
 * <p>Essa classe executa todo evento relacionado a minha classe de evento {@code OrderCancellationEvent}</p>
 */
@Component
public class NotificationCanceledOrderEmail {

//    Serviço de envio de email, as funções são explicadas dentro da classe
    @Autowired
    private SendEmailService sendEmailService;

//    Quando o pedido é cancelado, envia o evento
    @TransactionalEventListener
    private void whenCancelOrder (OrderCancellationEvent event) {

//        Recebe o pedido enviado pelo evento e trata os dados no serviço de envio de email
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
