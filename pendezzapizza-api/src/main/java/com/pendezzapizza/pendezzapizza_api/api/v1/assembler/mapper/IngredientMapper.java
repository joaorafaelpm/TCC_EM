package com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.IngredientModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.Ingredient;

@Mapper (componentModel = "spring")
public interface IngredientMapper {

    // lowStock não existe na entidade: é calculado
    @Mapping (target = "lowStock",
            expression = "java(ingredient.getStockQuantity() <= ingredient.getLowStockThreshold())")
    IngredientModel toModel(Ingredient ingredient);
}
