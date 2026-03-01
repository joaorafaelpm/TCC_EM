package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.RestaurantMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.RestaurantModel;
import com.pendezzapizza.pendezzapizza_api.core.security.PendezzaPizzaSecurity;
import com.pendezzapizza.pendezzapizza_api.domain.model.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class RestaurantModelAssembler{

    @Autowired
    private RestaurantMapper restaurantMapper;

    @Autowired
    private PendezzaPizzaSecurity pendezzaPizzaSecurity;


    public RestaurantModel toModel(Restaurant entity) {
        return restaurantMapper.toModel(entity);
    }

    public Collection<RestaurantModel> toCollectionModel(Collection<Restaurant> entities) {
        return entities.stream().map((this::toModel)).toList();
    }
}