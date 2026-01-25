package com.pendezzapizza.pendezzapizza_api.api.v1.assembler.disassambler;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.CityDTO;
import com.pendezzapizza.pendezzapizza_api.domain.model.City;
import org.mapstruct.*;
import org.springframework.context.annotation.Bean;

@Mapper(componentModel = "spring")
public interface CityDisassembler {

    @Bean
    @Mapping(source = "stateId", target = "state" )
    City cityDTOToCity (CityDTO cityDTO) ;

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateCityFromDto(CityDTO dto, @MappingTarget City entity);

}