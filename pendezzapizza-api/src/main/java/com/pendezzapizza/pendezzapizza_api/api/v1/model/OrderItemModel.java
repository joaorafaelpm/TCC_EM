package com.pendezzapizza.pendezzapizza_api.api.v1.model;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48")
    private UUID productId;
    @Schema(example = "Fettuccine Alfredo")
    private String productName;
    @Schema(example = "1")
    private Integer quantity;
    @Schema(example = "45.50")
    private BigDecimal unitPrice;
    @Schema(example = "45.50")
    private BigDecimal totalPrice;
    @Schema(example = "Com bastante parmesão, por favor")
    private String note;
}
