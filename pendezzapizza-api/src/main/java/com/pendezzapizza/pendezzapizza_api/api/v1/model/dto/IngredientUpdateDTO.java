package com.pendezzapizza.pendezzapizza_api.api.v1.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data 
public class IngredientUpdateDTO {

    @NotBlank 
    @Size (max = 120)
    private String name;

    @NotNull 
    private Long lowStockThreshold;
}
