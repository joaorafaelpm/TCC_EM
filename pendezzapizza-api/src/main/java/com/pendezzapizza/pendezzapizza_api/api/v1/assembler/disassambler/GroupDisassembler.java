package com.pendezzapizza.pendezzapizza_api.api.v1.assembler.disassambler;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.DTO.GroupDTO;
import com.pendezzapizza.pendezzapizza_api.domain.model.Group;
import org.mapstruct.*;
import org.springframework.context.annotation.Bean;


@Mapper(componentModel = "spring")
public interface GroupDisassembler {

    @Bean
    Group groupDTOToGroup (GroupDTO groupDTO) ;

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateGroupFromDto(GroupDTO dto, @MappingTarget Group entity);


}
