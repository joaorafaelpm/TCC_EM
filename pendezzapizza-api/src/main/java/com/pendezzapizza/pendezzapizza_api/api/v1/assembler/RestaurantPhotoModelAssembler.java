package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.RestaurantPhotoMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.PhotoModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.RestaurantPhoto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RestaurantPhotoModelAssembler {

    private RestaurantPhotoMapper restaurantPhotoMapper;

    public PhotoModel toModel(RestaurantPhoto photo) {
        return restaurantPhotoMapper.toModel(photo);
    }
}