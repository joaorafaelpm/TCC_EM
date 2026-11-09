package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import java.util.Collection;

import org.springframework.stereotype.Component;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.IngredientMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.IngredientModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.Ingredient;

import lombok.AllArgsConstructor;

@Component 
@AllArgsConstructor 
public class IngredientModelAssembler {

    private IngredientMapper ingredientMapper;

    public IngredientModel toModel(Ingredient ingredient) {
        return ingredientMapper.toModel(ingredient);
    }

    public Collection<IngredientModel> toCollectionModel(Collection<Ingredient> entities) {
        return entities.stream().map(this::toModel).toList();
    }
}