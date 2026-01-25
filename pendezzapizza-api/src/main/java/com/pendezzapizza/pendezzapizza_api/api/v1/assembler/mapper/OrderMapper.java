package com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.OrderItemModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.OrderModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.Order;
import com.pendezzapizza.pendezzapizza_api.domain.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Bean
    @Mapping(source = "orderStatus", target = "status")
    @Mapping(source = "deliveryAddress.city.state.name" , target = "deliveryAddress.city.state")
    @Mapping(source = "items" , target = "items" , qualifiedByName = "mapItems")
    OrderModel toModel(Order order);

    @Bean
    @Mapping(target = "productId" , source = "product.id")
    @Mapping(target = "productName" , source = "product.name")
    OrderItemModel orderItemToOrderItemModel (OrderItem orderItem);

    @Named("mapItems")
    default List<OrderItemModel> mapItems (List<OrderItem> items) {
        return items.stream().map(this::orderItemToOrderItemModel).collect(Collectors.toList());
    }

}