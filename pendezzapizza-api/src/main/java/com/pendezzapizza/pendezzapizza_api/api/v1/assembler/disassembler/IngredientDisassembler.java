package com.pendezzapizza.pendezzapizza_api.api.v1.assembler.disassembler;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.IngredientDTO;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.IngredientUpdateDTO;
import com.pendezzapizza.pendezzapizza_api.domain.model.Ingredient;

@Mapper (componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IngredientDisassembler {

    // initialQuantity não tem campo na entidade: vira ENTRADA via StockService
    @Mapping (target = "id", ignore = true)
    @Mapping(target = "restaurant", ignore = true)
    @Mapping(target = "stockQuantity", ignore = true)
    Ingredient ingredientDTOToIngredient(IngredientDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "restaurant", ignore = true)
    @Mapping(target = "unit", ignore = true)
    @Mapping(target = "stockQuantity", ignore = true)
    Ingredient ingredientUpdateDTOToIngredient(IngredientUpdateDTO dto);
}
