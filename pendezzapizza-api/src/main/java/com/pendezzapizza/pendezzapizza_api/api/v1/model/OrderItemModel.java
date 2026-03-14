package com.pendezzapizza.pendezzapizza_api.api.v1.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;
import java.util.UUID;


@Relation(collectionRelation = "orderItems")
@Getter
@Setter
@AllArgsConstructor
public class OrderItemModel extends RepresentationModel<OrderItemModel> {

    private UUID productId ;
    private String productName ;
    private Integer quantity ;
    private BigDecimal unityPrice ;
    private BigDecimal totalPrice ;
    private String observation;

}
