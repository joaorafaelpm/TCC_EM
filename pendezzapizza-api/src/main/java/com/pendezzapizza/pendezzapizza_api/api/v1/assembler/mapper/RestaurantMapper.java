package com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.RestaurantModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.context.annotation.Bean;

/**
 * Mapeador da entidade de restaurante usando a biblioteca Mapstruct, que requer a anotação {@code Mapper} para funcionar corretamente
 *
 * <p>Toda diferença entre o modelo e a entidade original é mapeada nos métodos da interface</p>
 */
@Mapper(componentModel = "spring")
public interface RestaurantMapper {

//    Como a forma que eu trato o endereço é diferente do modelo para o original, eu preciso mapear
    @Bean
    @Mapping(source = "address.city.state.name" , target = "address.city.state")
    RestaurantModel toModel(Restaurant restaurant);

}