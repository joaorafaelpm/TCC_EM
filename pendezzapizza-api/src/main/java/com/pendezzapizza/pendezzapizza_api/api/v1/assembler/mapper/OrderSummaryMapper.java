package com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.OrderSummaryModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.context.annotation.Bean;

/**
 * Mapeador da entidade de resumo de pedido usando a biblioteca Mapstruct, que requer a anotação {@code Mapper} para funcionar corretamente
 *
 * <p>Toda diferença entre o modelo e a entidade original é mapeada nos métodos da interface</p>
 */
@Mapper(componentModel = "spring")
public interface OrderSummaryMapper {

//    Mapeio todas as diferenças de entidades da minha original para a de modelo
    @Bean
    @Mapping(source = "orderStatus", target = "status")
    @Mapping(source = "cancellationDate", target = "canceledAt")
    @Mapping(source = "confirmationDate", target = "confirmedAt")
    @Mapping(source = "creationDate", target = "createdAt")
    @Mapping(source = "deliveryDate", target = "deliveredAt")
    @Mapping(source = "totalCost", target = "totalValue")
    OrderSummaryModel toModel(Order order);

}