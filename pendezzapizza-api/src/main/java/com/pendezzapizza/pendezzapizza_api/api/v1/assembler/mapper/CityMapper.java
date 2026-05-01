package com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.CityModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.City;
import org.mapstruct.Mapper;
import org.springframework.context.annotation.Bean;

// Precisa disso para avisar o mapstruct que estamos usando um modelo spring, se não, não funciona

/**
 * Mapeador da entidade de cidade usando a biblioteca Mapstruct, que requer a anotação {@code Mapper} para funcionar corretamente
 *
 * <p>Toda diferença entre o modelo e a entidade original é mapeada nos métodos da interface</p>
 */
@Mapper(componentModel = "spring")
public interface CityMapper {

    @Bean
    CityModel toModel(City city);


}