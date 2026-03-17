package com.pendezzapizza.pendezzapizza_api.api.v1.assembler.disassambler;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.OrderDTO;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.OrderItemDTO;
import com.pendezzapizza.pendezzapizza_api.domain.model.Order;
import com.pendezzapizza.pendezzapizza_api.domain.model.OrderItem;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderDisassembler {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "restaurantId", target = "restaurant")
    @Mapping(target = "restaurant.name", ignore = true)
    @Mapping(target = "restaurant.shippingFee", ignore = true)
    @Mapping(target = "restaurant.address", ignore = true)
    @Mapping(target = "restaurant.active", ignore = true)
    @Mapping(target = "restaurant.open", ignore = true)
    @Mapping(target = "restaurant.registrationDate", ignore = true)
    @Mapping(target = "restaurant.updateDate", ignore = true)
    @Mapping(target = "restaurant.products", ignore = true)
    @Mapping(target = "restaurant.paymentMethods", ignore = true)
    @Mapping(target = "restaurant.responsibleUsers", ignore = true)
    @Mapping(source = "paymentMethodId", target = "paymentMethod")
    @Mapping(target = "paymentMethod.description", ignore = true)
    @Mapping(target = "paymentMethod.updateDate", ignore = true)
    @Mapping(target = "deliveryAddress.city.name", ignore = true)
    @Mapping(target = "deliveryAddress.city.state", ignore = true)
    @Mapping(target = "deliveryAddress.city.updateDate", ignore = true)
    @Mapping(source = "items", target = "items", qualifiedByName = "mapItems")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "subtotal", ignore = true)
    @Mapping(target = "shippingFee", ignore = true)
    @Mapping(target = "totalCost", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "confirmationDate", ignore = true)
    @Mapping(target = "cancellationDate", ignore = true)
    @Mapping(target = "deliveryDate", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    Order orderDTOToOrder(OrderDTO orderDTO);


    // Mapeia de DTO → Entidade
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "product.id", source = "productId")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "unitPrice", ignore = true)
    OrderItem orderItemDTOToOrderItem(OrderItemDTO orderItemDTO);

    // Mapeia lista de DTOs → lista de entidades
    @Named("mapItems")
    default List<OrderItem> mapItems(List<OrderItemDTO> itemsDTO) {
        return itemsDTO.stream()
                .map(this::orderItemDTOToOrderItem)
                .toList();
    }
}

