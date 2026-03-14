package com.pendezzapizza.pendezzapizza_api.api.v1.assembler.disassambler;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.DTO.RestaurantDTO;
import com.pendezzapizza.pendezzapizza_api.domain.model.Restaurant;
import org.mapstruct.*;
import org.springframework.context.annotation.Bean;


@Mapper(componentModel = "spring")
public interface RestaurantDisassembler {

    @Bean
    Restaurant restaurantDTOToRestaurant (RestaurantDTO restaurantDTO) ;

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateRestaurantFromDto(RestaurantDTO dto, @MappingTarget Restaurant entity);


}
