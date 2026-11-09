package com.pendezzapizza.pendezzapizza_api.api.v1.model.dto;

import com.pendezzapizza.pendezzapizza_api.domain.model.enums.UnitType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class IngredientDTO {

    @NotBlank
    @Size(max = 120)
    private String name;

    @NotNull
    private UnitType unit; // G, ML ou UN

    /** Opcional. Se vier nulo, vira 0. Unidade base (g, ml, un). */
    @PositiveOrZero
    private Long lowStockThreshold;

    /** Opcional. Se vier nulo, vira 0. Unidade base (g, ml, un). */
    @PositiveOrZero
    private Long initialQuantity;
}
