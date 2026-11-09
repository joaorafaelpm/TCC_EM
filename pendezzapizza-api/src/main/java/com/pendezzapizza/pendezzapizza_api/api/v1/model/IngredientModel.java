package com.pendezzapizza.pendezzapizza_api.api.v1.model;

import java.util.UUID;

import com.pendezzapizza.pendezzapizza_api.domain.model.Ingredient;
import com.pendezzapizza.pendezzapizza_api.domain.model.enums.UnitType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor 
@AllArgsConstructor 
public class IngredientModel {
    private UUID id;
    private String name;
    private UnitType unit;
    private long stockQuantity;
    private long lowStockThreshold;
    private boolean lowStock;
}