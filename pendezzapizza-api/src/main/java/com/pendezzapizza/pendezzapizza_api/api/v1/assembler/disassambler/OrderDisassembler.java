package com.pendezzapizza.pendezzapizza_api.api.v1.assembler.disassambler;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.OrderDTO;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.OrderItemDTO;
import com.pendezzapizza.pendezzapizza_api.domain.model.Order;
import com.pendezzapizza.pendezzapizza_api.domain.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderDisassembler {

    @Mapping(source = "restaurantId", target = "restaurant")
    @Mapping(source = "paymentMethodId", target = "paymentMethods")
    @Mapping(source = "items", target = "items", qualifiedByName = "mapItems")
    Order orderDTOToOrder(OrderDTO OrderDTO);


    // Mapeia de DTO → Entidade
    @Mapping(target = "product.id", source = "productId")
    OrderItem orderItemDTOToOrderItem(OrderItemDTO orderItemDTO);

    // Mapeia lista de DTOs → lista de entidades
    @Named("mapItems")
    default List<OrderItem> mapItems(List<OrderItemDTO> itemsDTO) {
        return itemsDTO.stream()
                .map(this::orderItemDTOToOrderItem)
                .collect(Collectors.toList());
    }
}

