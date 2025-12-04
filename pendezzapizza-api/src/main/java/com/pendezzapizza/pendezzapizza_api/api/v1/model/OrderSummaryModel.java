package com.pendezzapizza.pendezzapizza_api.api.v1.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Relation(collectionRelation = "orders")
@Getter
@Setter
@AllArgsConstructor
public class OrderSummaryModel extends RepresentationModel<OrderSummaryModel> {

    private UUID id;
    private BigDecimal subtotal ;
    private BigDecimal shippingFee ;
    private BigDecimal totalCost ;
    private OffsetDateTime creationDate;
    private OffsetDateTime confirmationDate ;
    private OffsetDateTime cancellationDate;
    private OffsetDateTime deliveryDate;
    private RestaurantJustNameModel restaurant;
    private UserModel client ;

}
