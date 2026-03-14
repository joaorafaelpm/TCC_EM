package com.pendezzapizza.pendezzapizza_api.api.v1.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.UUID;

@Relation(collectionRelation = "paymentMethods")
@Getter
@Setter
@AllArgsConstructor
public class PaymentMethodModel extends RepresentationModel<PaymentMethodModel> {

    private UUID id ;
    private String description;
}
