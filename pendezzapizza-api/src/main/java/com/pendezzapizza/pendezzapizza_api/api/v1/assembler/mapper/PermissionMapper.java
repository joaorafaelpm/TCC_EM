package com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.PermissionModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.Permission;
import org.mapstruct.Mapper;
import org.springframework.context.annotation.Bean;


@Mapper(componentModel = "spring")
public interface PermissionMapper {

    @Bean
    PermissionModel toModel(Permission permission);

}