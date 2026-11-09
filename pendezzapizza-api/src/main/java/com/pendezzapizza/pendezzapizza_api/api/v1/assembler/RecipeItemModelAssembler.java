package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import java.util.Collection;

import org.springframework.stereotype.Component;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.RecipeItemMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.RecipeItemModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.RecipeItem;

import lombok.AllArgsConstructor;

@Component 
@AllArgsConstructor 
public class RecipeItemModelAssembler {

    private RecipeItemMapper recipeItemMapper;

    public RecipeItemModel toModel(RecipeItem item) {
        return recipeItemMapper.toModel(item);
    }

    public Collection<RecipeItemModel> toCollectionModel(Collection<RecipeItem> entities) {
        return entities.stream().map(this::toModel).toList();
    }
}