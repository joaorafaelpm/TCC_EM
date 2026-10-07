package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.RestaurantMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.RestaurantModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.Restaurant;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Assembler da minha entidade de <b>restaurante</b>
 *
 * <p>Essa é uma classe auxiliar que serve para usar o mapper de forma indireta</p>
 * <p>Opto por não usar o mapper direto para abrir a possibilidade de implementação de links (a gosto do freguês). Ou simplesmente adicionar lógica aqui dentro caso seja necessário </p>
 */
@Component
@AllArgsConstructor
public class RestaurantModelAssembler{

    private RestaurantMapper restaurantMapper;

    public RestaurantModel toModel(Restaurant entity) {
        return restaurantMapper.toModel(entity);
    }

    public Collection<RestaurantModel> toCollectionModel(Collection<Restaurant> entities) {
        return entities.stream().map((this::toModel)).toList();
    }
}