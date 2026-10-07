package com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.ProductModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.context.annotation.Bean;

/**
 * Mapeador da entidade de produto usando a biblioteca Mapstruct, que requer a anotação {@code Mapper} para funcionar corretamente
 *
 * <p>Toda diferença entre o modelo e a entidade original é mapeada nos métodos da interface</p>
 */
@Mapper(componentModel = "spring")
public interface ProductMapper {

//    Para encontrar e retornar a entidade de restaurante é necessário especificar e mapear o id de restaurante acessando a entidade e depois o ID
    @Bean
    @Mapping(source = "restaurant.id" , target = "restaurantId")
    ProductModel toModel(Product product);

}