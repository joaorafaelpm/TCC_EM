package com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.StateModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.State;
import org.mapstruct.Mapper;
import org.springframework.context.annotation.Bean;

/**
 * Mapeador da entidade de estado usando a biblioteca Mapstruct, que requer a anotação {@code Mapper} para funcionar corretamente
 *
 * <p>Toda diferença entre o modelo e a entidade original é mapeada nos métodos da interface</p>
 */
@Mapper(componentModel = "spring")
public interface StateMapper {

    @Bean
    StateModel toModel(State state);

}