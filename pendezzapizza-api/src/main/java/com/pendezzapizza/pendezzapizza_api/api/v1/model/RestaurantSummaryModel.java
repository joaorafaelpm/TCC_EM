package com.pendezzapizza.pendezzapizza_api.api.v1.model;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class RestaurantSummaryModel extends RepresentationModel<RestaurantSummaryModel> {

    @Schema(example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48")
    private UUID id;

    @Schema(example = "Thai Gourmet")
    private String name;

    @Schema(example = "10.00")
    private BigDecimal shippingFee;

}


