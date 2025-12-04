package com.pendezzapizza.pendezzapizza_api.api.v1.assembler.disassambler;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.DTO.UserDTO;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.DTO.UserWithPasswordDTO;
import com.pendezzapizza.pendezzapizza_api.domain.model.User;
import org.mapstruct.*;
import org.springframework.context.annotation.Bean;


@Mapper(componentModel = "spring")
public interface UserDisassembler {

    @Bean
    User userDTOToUser (UserDTO userDTO) ;

    @Bean
    @Mapping(target = "password" , source = "password")
    User userWithPasswordDTOToUser (UserWithPasswordDTO userWithPasswordDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateUserFromDto(UserDTO dto, @MappingTarget User entity);


}

