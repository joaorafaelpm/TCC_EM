package com.pendezzapizza.pendezzapizza_api.api.v1.assembler.disassambler;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.DTO.StateDTO;
import com.pendezzapizza.pendezzapizza_api.domain.model.State;
import org.mapstruct.*;
import org.springframework.context.annotation.Bean;

@Mapper(componentModel = "spring")
public interface StateDisassembler {

    @Bean
    State stateDTOToState (StateDTO stateDTO) ;

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateStateFromDto(StateDTO dto, @MappingTarget State entity);

}
