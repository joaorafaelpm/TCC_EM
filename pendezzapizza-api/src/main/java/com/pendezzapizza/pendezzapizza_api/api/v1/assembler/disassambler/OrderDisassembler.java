package com.pendezzapizza.pendezzapizza_api.api.v1.assembler.disassambler;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.DTO.OrderDTO;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.DTO.OrderItemDTO;
import com.pendezzapizza.pendezzapizza_api.domain.model.Order;
import com.pendezzapizza.pendezzapizza_api.domain.model.OrderItem;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderDisassembler {

    @Mapping(source = "restaurantId", target = "restaurant")
    @Mapping(source = "clientId", target = "client")
    @Mapping(source = "paymentMethodId", target = "paymentMethods")
    @Mapping(source = "items", target = "items", qualifiedByName = "mapItems")
    Order orderDTOToOrder(OrderDTO orderDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateOrderFromDto(OrderDTO dto, @MappingTarget Order entity);

    @Mapping(target = "product.id", source = "productId")
    OrderItem orderItemDTOToOrderItem(OrderItemDTO orderItemDTO);

    @Named("mapItems")
    default List<OrderItem> mapItems(List<OrderItemDTO> itemsDTO) {
        return itemsDTO.stream()
                .map(this::orderItemDTOToOrderItem)
                .collect(Collectors.toList());
    }
}

