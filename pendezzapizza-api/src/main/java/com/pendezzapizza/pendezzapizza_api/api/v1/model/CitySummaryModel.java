/**
 * @summary     Representa o modelo de resposta resumido de uma cidade, utilizado como DTO aninhado
 *              em outros modelos de resposta para evitar payload excessivo e ciclos de serialização.
 * @difficulty  Low
 * @depends-on  None
 */
package com.pendezzapizza.pendezzapizza_api.api.v1.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class CitySummaryModel {

    @Schema(example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48")
    private UUID id;

    @Schema(example = "Campinas")
    private String name;

    // Estado representado como String simples (nome) em vez de StateModel
    // para manter o payload mínimo quando a cidade aparece como dado secundário em uma resposta
    @Schema(example = "São Paulo")
    private String state;
}