package com.pendezzapizza.pendezzapizza_api.api.v1.assembler.disassambler;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.CityDTO;
import com.pendezzapizza.pendezzapizza_api.domain.model.City;
import org.mapstruct.*;
import org.springframework.context.annotation.Bean;

/**
 * Disassembler da entidade de cidade usando a biblioteca Mapstruct, que requer a anotação {@code Mapper} para funcionar corretamente
 *
 * <p>Faço o mapeamento partindo da minha entidade de DTO para a minha original</p>
 * <p>Toda diferença entre o modelo e a entidade original é mapeada nos métodos da interface e para evitar warning é <b>necessário<b> mapear cada um dos parâmetros mesmo que seja o mesmo</p>
 */
@Mapper(componentModel = "spring")
public interface CityDisassembler {

    @Bean
    @Mapping(source = "stateId", target = "state" )
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "name", source = "name")
    City cityDTOToCity (CityDTO cityDTO) ;

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "stateId", target = "state")
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "name", source = "name")
    void updateCityFromDto(CityDTO dto, @MappingTarget City entity);

}