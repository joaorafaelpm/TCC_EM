package com.pendezzapizza.pendezzapizza_api.api.v1.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Classe de DTO para representação da entidade de <b>endereço</b>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {

    @Schema(example = "13068-603")
    @NotBlank
    private String zipCode;

    @Schema(example = "Rua Sta. Luzia")
    @NotBlank
    private String street;

    @Schema(example = "109")
    @NotBlank
    private String number;

    @Schema(example = "Caixa d'gua Sanasa")
    private String complement;

    @Schema(example = "Jardim Aparecida")
    @NotBlank
    private String neighborhood;

    @NotNull
    @Valid
    private CityIdDTO city;
}