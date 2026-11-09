package com.pendezzapizza.pendezzapizza_api.api.v1.model.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class RecipeItemDTO {

    @NotNull
    private UUID ingredientId;

    /** Quantidade na unidade base consumida por 1 unidade do produto. Ex.: 500 (g) por pizza. */
    @NotNull
    @Positive
    private Long quantityPerUnit;
}