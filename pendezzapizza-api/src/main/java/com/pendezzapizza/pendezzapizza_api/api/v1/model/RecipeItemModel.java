package com.pendezzapizza.pendezzapizza_api.api.v1.model;

import java.util.UUID;

import com.pendezzapizza.pendezzapizza_api.domain.model.RecipeItem;
import com.pendezzapizza.pendezzapizza_api.domain.model.enums.UnitType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeItemModel {
    private UUID ingredientId;
    private String ingredientName;
    private UnitType unit;
    private long quantityPerUnit;
}