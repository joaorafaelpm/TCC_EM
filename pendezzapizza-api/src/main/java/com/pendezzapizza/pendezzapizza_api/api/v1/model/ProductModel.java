package com.pendezzapizza.pendezzapizza_api.api.v1.model;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48")
    private UUID id;

    @Schema(example = "Porco com molho agridoce")
    private String name;

    @Schema(example = "Deliciosa carne suína ao molho especial")
    private String description;

    @Schema(example = "78.90")
    private BigDecimal price;

    @Schema(example = "true")
    private Boolean active;
}