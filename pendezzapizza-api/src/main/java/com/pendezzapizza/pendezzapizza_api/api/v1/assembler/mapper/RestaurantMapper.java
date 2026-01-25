package com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.RestaurantModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.context.annotation.Bean;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {

    @Bean
    @Mapping(source = "address.city.state.name" , target = "address.city.state")
    RestaurantModel toModel(Restaurant restaurant);

}