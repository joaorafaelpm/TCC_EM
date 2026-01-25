package com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.StateModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.State;
import org.mapstruct.Mapper;
import org.springframework.context.annotation.Bean;

@Mapper(componentModel = "spring")
public interface StateMapper {

    @Bean
    StateModel toModel(State state);

}