package com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.RestaurantJustNameModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.RestaurantSummaryModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.Restaurant;
import org.mapstruct.Mapper;
import org.springframework.context.annotation.Bean;

@Mapper(componentModel = "spring")
public interface RestaurantSummaryMapper {

    @Bean
    RestaurantSummaryModel toModel(Restaurant restaurant);

    @Bean
    RestaurantJustNameModel toModelSummary (Restaurant restaurant);

}