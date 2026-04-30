package com.pendezzapizza.pendezzapizza_api.api.v1.assembler.disassambler;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.UserDTO;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.UserWithPasswordDTO;
import com.pendezzapizza.pendezzapizza_api.domain.model.User;
import org.mapstruct.*;
import org.springframework.context.annotation.Bean;


@Mapper(componentModel = "spring")
public interface UserDisassembler {

    @Bean
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "groups", ignore = true)
    User userDTOToUser (UserDTO userDTO) ;

    @Bean
    @Mapping(target = "password" , source = "password")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "groups", ignore = true)
    User userWithPasswordDTOToUser (UserWithPasswordDTO userWithPasswordDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "groups", ignore = true)
    void updateUserFromDto(UserDTO dto, @MappingTarget User entity);


}