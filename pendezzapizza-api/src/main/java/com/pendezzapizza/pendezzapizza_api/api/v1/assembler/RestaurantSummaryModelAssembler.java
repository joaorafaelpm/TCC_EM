package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.RestaurantSummaryMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.RestaurantSummaryModel;
import com.pendezzapizza.pendezzapizza_api.core.security.PendezzaPizzaSecurity;
import com.pendezzapizza.pendezzapizza_api.domain.model.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class RestaurantSummaryModelAssembler {

    @Autowired
    private RestaurantSummaryMapper restaurantMapper;

    @Autowired
    private PendezzaPizzaSecurity pendezzaPizzaSecurity;


    public RestaurantSummaryModel toModel(Restaurant entity) {
        return restaurantMapper.toModel(entity);
    }

    public Collection<RestaurantSummaryModel> toCollectionModel(Collection<Restaurant> entities) {
        return entities.stream().map((this::toModel)).toList();
    }
}