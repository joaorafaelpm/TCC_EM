package com.pendezzapizza.pendezzapizza_api.api.v1.model;

import com.pendezzapizza.pendezzapizza_api.domain.model.enuns.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Relation(collectionRelation = "orders")
@Getter
@Setter
@AllArgsConstructor
public class OrderModel extends RepresentationModel<OrderModel> {

    private UUID id;

    private BigDecimal subtotal ;
    private BigDecimal shippingFee ;
    private BigDecimal totalCost ;
    private OffsetDateTime creationDate;
    private OffsetDateTime confirmationDate ;
    private OffsetDateTime cancellationDate;
    private OffsetDateTime deliveryDate;

    private OrderStatus orderStatus ;
    private UserModel client ;

    private RestaurantSummaryModel restaurant ;



    private AddressModel deliveryAddress ;

    private PaymentMethodModel paymentMethod ;

    private List<OrderItemModel> items;

}
