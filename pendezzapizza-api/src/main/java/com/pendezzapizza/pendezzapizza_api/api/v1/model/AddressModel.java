/**
 * @summary     Representa o modelo de resposta de um endereço completo retornado pela API.
 *              Utilizado como DTO de saída em recursos que expõem dados de endereço.
 * @difficulty  Low
 * @depends-on  None
 */
package com.pendezzapizza.pendezzapizza_api.api.v1.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class AddressModel {

    // CEP no formato XXXXX-XXX, utilizado apenas para exibição — sem validação de formato neste modelo
    @Schema(example = "13068-603")
    private String zipCode;

    @Schema(example = "Rua Sta. Luzia")
    private String street;

    @Schema(example = "109")
    private String number;

    @Schema(example = "Caixa d'gua Sanasa")
    private String complement;

    @Schema(example = "Jardim Aparecida")
    private String neighborhood;

    // Cidade representada como resumo para evitar ciclos de serialização e reduzir o payload de resposta
    private CitySummaryModel city;
}