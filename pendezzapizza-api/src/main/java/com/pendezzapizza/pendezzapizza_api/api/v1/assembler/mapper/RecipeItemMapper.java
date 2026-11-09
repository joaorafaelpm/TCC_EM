package com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.RecipeItemModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.RecipeItem;

@Mapper (componentModel = "spring")
public interface RecipeItemMapper {

    @Mapping (source = "ingredient.id", target = "ingredientId")
    @Mapping(source = "ingredient.name", target = "ingredientName")
    @Mapping(source = "ingredient.unit", target = "unit")
    RecipeItemModel toModel(RecipeItem recipeItem);
}
