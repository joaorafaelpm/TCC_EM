package com.pendezzapizza.pendezzapizza_api.api.v1.assembler.disassambler;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.RestaurantUpdateDTO;
import com.pendezzapizza.pendezzapizza_api.domain.model.Restaurant;
import org.mapstruct.*;

/**
 * Disassembler da entidade de restaurante usando a biblioteca Mapstruct, que requer a anotação {@code Mapper} para funcionar corretamente
 *
 * <p>Faço o mapeamento partindo da minha entidade de DTO para a minha original</p>
 * <p>Toda diferença entre o modelo e a entidade original é mapeada nos métodos da interface e para evitar warning é <b>necessário<b> mapear cada um dos parâmetros mesmo que seja o mesmo</p>
 */
@Mapper(componentModel = "spring",unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface RestaurantUpdateDisassembler {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Restaurant restaurantUpdateDTOToRestaurant (RestaurantUpdateDTO restaurantDTO) ;

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateRestaurantFromDto(RestaurantUpdateDTO dto, @MappingTarget Restaurant entity);


}