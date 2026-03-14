package com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.CityModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.City;
import org.mapstruct.Mapper;
import org.springframework.context.annotation.Bean;

@Mapper(componentModel = "spring")
public interface CityMapper {

    @Bean
    CityModel toModel(City city);


}
