package com.pendezzapizza.pendezzapizza_api.api.v1.assembler.disassambler;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.StateDTO;
import com.pendezzapizza.pendezzapizza_api.domain.model.State;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface StateDisassembler {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cities", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    State stateDTOToState (StateDTO stateDTO) ;

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cities", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    void updateStateFromDto(StateDTO dto, @MappingTarget State entity);

}