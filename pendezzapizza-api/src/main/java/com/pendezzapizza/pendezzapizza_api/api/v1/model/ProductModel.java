package com.pendezzapizza.pendezzapizza_api.api.v1.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;
import java.util.UUID;

@Relation(collectionRelation = "products")
@Getter
@Setter
@AllArgsConstructor
public class ProductModel extends RepresentationModel<ProductModel> {

    private UUID id ;
    private String name ;
    private String description ;
    private BigDecimal price ;
    private Boolean active;

}
