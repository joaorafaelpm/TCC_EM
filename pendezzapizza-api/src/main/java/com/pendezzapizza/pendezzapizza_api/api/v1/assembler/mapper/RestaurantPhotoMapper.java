package com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.PhotoModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.RestaurantPhoto;
import org.mapstruct.Mapper;
import org.springframework.context.annotation.Bean;

@Mapper(componentModel = "spring")
public interface RestaurantPhotoMapper {

    @Bean
    PhotoModel toModel(RestaurantPhoto restaurantPhoto);

}