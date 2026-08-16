package com.pendezzapizza.pendezzapizza_api.api.v1.assembler.disassembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.UserDTO;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.UserWithPasswordDTO;
import com.pendezzapizza.pendezzapizza_api.domain.model.User;
import org.mapstruct.*;
import org.springframework.context.annotation.Bean;

/**
 * Disassembler da entidade de usuário usando a biblioteca Mapstruct, que requer a anotação {@code Mapper} para funcionar corretamente
 *
 * <p>Faço o mapeamento partindo da minha entidade de DTO para a minha original</p>
 * <p>Toda diferença entre o modelo e a entidade original é mapeada nos métodos da interface e para evitar warning é <b>necessário<b> mapear cada um dos parâmetros mesmo que seja o mesmo</p>
 */
@Mapper(componentModel = "spring")
public interface UserDisassembler {

    @Bean
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "groups", ignore = true)
    @Mapping(target = "userRestaurants", ignore = true)
    User userDTOToUser (UserDTO userDTO) ;

    @Bean
    @Mapping(target = "password" , source = "password")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "groups", ignore = true)
    @Mapping(target = "userRestaurants", ignore = true)
    User userWithPasswordDTOToUser (UserWithPasswordDTO userWithPasswordDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "groups", ignore = true)
    @Mapping(target = "userRestaurants", ignore = true)
    void updateUserFromDto(UserDTO dto, @MappingTarget User entity);


}