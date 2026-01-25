package com.pendezzapizza.pendezzapizza_api.api.v1.model;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(example = "936dc9ec-05bf-44e5-8c07-7e51adc6083d")
    private UUID id;

    @Schema(example = "298.90")
    private BigDecimal subtotal;

    @Schema(example = "10.00")
    private BigDecimal shippingFee;

    @Schema(example = "308.90")
    private BigDecimal totalValue;

    @Schema(example = "CREATED")
    private String status;

    @Schema(example = "2022-12-01T20:34:04Z")
    private OffsetDateTime createdAt;

    @Schema(example = "2022-12-01T20:35:10Z")
    private OffsetDateTime confirmedAt;

    @Schema(example = "2022-12-01T20:55:30Z")
    private OffsetDateTime deliveredAt;

    @Schema(example = "2022-12-01T20:35:00Z")
    private OffsetDateTime canceledAt;

    private RestaurantSummaryModel restaurant;
    private UserModel customer;
}