package com.pendezzapizza.pendezzapizza_api.api.v1.assembler.disassambler;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.GroupDTO;
import com.pendezzapizza.pendezzapizza_api.domain.model.Group;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface GroupDisassembler {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "permission", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    Group groupDTOToGroup (GroupDTO groupDTO) ;

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "permission", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    void updateGroupFromDto(GroupDTO dto, @MappingTarget Group entity);


}