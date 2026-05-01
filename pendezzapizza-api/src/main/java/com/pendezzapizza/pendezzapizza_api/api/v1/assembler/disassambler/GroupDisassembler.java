package com.pendezzapizza.pendezzapizza_api.api.v1.assembler.disassambler;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.GroupDTO;
import com.pendezzapizza.pendezzapizza_api.domain.model.Group;
import org.mapstruct.*;


/**
 * Disassembler da entidade de grupo usando a biblioteca Mapstruct, que requer a anotação {@code Mapper} para funcionar corretamente
 *
 * <p>Faço o mapeamento partindo da minha entidade de DTO para a minha original</p>
 * <p>Toda diferença entre o modelo e a entidade original é mapeada nos métodos da interface e para evitar warning é <b>necessário<b> mapear cada um dos parâmetros mesmo que seja o mesmo</p>
 */
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