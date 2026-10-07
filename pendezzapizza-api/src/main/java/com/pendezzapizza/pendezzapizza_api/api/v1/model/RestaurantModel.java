/**
 * @summary     Representa o modelo de resposta completo de um restaurante retornado pela API.
 *              Inclui dados operacionais, endereço e flags de estado do restaurante.
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
public class RestaurantModel {

    @Schema(example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48")
    private UUID id;

    @Schema(example = "Thai Gourmet")
    private String name;

    @Schema(example = "10.00")
    private BigDecimal shippingFee;

    // Controla se o restaurante está habilitado na plataforma — restaurantes inativos
    // não aceitam pedidos independentemente do flag open
    private Boolean active;

    // Controla se o restaurante está aberto no momento — um restaurante pode estar
    // ativo mas fechado; pedidos só são aceitos quando ambos os flags são true
    private Boolean open;

    private AddressModel address;
}