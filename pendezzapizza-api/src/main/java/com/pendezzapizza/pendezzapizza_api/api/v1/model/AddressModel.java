package com.pendezzapizza.pendezzapizza_api.api.v1.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AddressModel {

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

    private CitySummaryModel city;
}
