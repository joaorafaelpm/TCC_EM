/**
 * @summary     Representa o modelo de resposta resumido de um restaurante, utilizado como DTO aninhado
 *              em pedidos e outros contextos onde apenas dados essenciais do restaurante são necessários.
 * @difficulty  Low
 * @depends-on  None
 */
package com.pendezzapizza.pendezzapizza_api.api.v1.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class RestaurantSummaryModel {

    @Schema(example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48")
    private UUID id;

    @Schema(example = "Thai Gourmet")
    private String name;

    // Taxa de entrega exposta no resumo pois é dado relevante para exibição em listagens de pedidos
    @Schema(example = "10.00")
    private BigDecimal shippingFee;
}