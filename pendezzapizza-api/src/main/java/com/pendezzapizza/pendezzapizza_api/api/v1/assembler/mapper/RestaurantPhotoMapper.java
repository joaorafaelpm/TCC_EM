package com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.PhotoModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.RestaurantPhoto;
import org.mapstruct.Mapper;
import org.springframework.context.annotation.Bean;

/**
 * Mapeador da entidade de foto de restaurante usando a biblioteca Mapstruct, que requer a anotação {@code Mapper} para funcionar corretamente
 *
 * <p>Toda diferença entre o modelo e a entidade original é mapeada nos métodos da interface</p>
 */
@Mapper(componentModel = "spring")
public interface RestaurantPhotoMapper {

    @Bean
    PhotoModel toModel(RestaurantPhoto restaurantPhoto);

}