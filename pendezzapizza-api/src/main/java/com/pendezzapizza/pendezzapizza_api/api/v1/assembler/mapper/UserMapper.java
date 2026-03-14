package com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.UserModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.User;
import org.mapstruct.Mapper;
import org.springframework.context.annotation.Bean;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Bean
    UserModel toModel(User user);


}

