package com.pendezzapizza.pendezzapizza_api.api.v1.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;
import java.util.UUID;

@Relation(collectionRelation = "restaurants")
@Getter
@Setter
@AllArgsConstructor
public class RestaurantModel extends RepresentationModel<RestaurantModel> {

    private UUID id;
    private String name;
    private BigDecimal shippingFee;

    private Boolean active ;
    private Boolean open ;
    private AddressModel address ;

}


