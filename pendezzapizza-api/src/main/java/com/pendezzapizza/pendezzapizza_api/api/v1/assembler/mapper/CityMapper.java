package com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.CityModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.City;
import org.mapstruct.Mapper;
import org.springframework.context.annotation.Bean;

// Precisa disso para avisar o mapstruct que estamos usando um modelo spring, se não, não funciona
@Mapper(componentModel = "spring")
public interface CityMapper {

    @Bean
    CityModel toModel(City city);


}