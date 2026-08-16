package com.pendezzapizza.pendezzapizza_api.api.v1.assembler.disassembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.RestaurantDTO;
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
public interface RestaurantDisassembler {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    O cpf vai ser usado somente na ligação de RestaurantOwnerProfile e não no restaurante em si.
//    Opto por isso pelo fato de poder ter um restaurante com mais de um dono e a facilidade de controlar uma terceira tabela indiretamente pela entidade de restaurante
    Restaurant restaurantDTOToRestaurant (RestaurantDTO restaurantDTO) ;

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRestaurantFromDto(RestaurantUpdateDTO dto, @MappingTarget Restaurant entity);


}