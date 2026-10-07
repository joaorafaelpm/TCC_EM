/**
 * @summary     Representa o modelo de resposta de um item individual dentro de um pedido retornado pela API.
 *              Expõe dados de produto, quantidade, preços unitário e total, e observação do cliente.
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
public class OrderItemModel {

    @Schema(example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48")
    private UUID productId;

    @Schema(example = "Fettuccine Alfredo")
    private String productName;

    @Schema(example = "1")
    private Integer quantity;

    // Preço unitário no momento do pedido — pode diferir do preço atual do produto
    // caso o valor tenha sido alterado após a realização do pedido
    @Schema(example = "45.50")
    private BigDecimal unitPrice;

    // Valor pré-calculado e persistido (quantity × unitPrice) — não deve ser recalculado
    // no frontend, pois reflete o preço vigente no momento do pedido
    @Schema(example = "45.50")
    private BigDecimal totalPrice;

    @Schema(example = "Com bastante parmesão, por favor")
    private String note;
}