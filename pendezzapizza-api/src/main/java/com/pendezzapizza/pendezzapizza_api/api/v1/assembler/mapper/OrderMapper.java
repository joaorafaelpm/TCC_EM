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

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Bean
//    Esse mapping literalmente mapeia de um objeto (source = de onde vem) para o objeto final (target = para onde vai). Isso vale para TODOS os mappings do Mapper
    @Mapping(source = "orderStatus", target = "status")
    @Mapping(source = "deliveryAddress.city.state.name" , target = "deliveryAddress.city.state")
    @Mapping(source = "items" , target = "items" , qualifiedByName = "mapItems")
    @Mapping(source = "cancellationDate", target = "canceledAt")
    @Mapping(source = "confirmationDate", target = "confirmedAt")
    @Mapping(source = "creationDate", target = "createdAt")
    @Mapping(source = "deliveryDate", target = "deliveredAt")
    @Mapping(source = "totalCost", target = "totalValue")
    OrderModel toModel(Order order);

    @Bean
    @Mapping(target = "productId" , source = "product.id")
    @Mapping(target = "productName" , source = "product.name")
    OrderItemModel orderItemToOrderItemModel (OrderItem orderItem);

    @Named("mapItems")
    default List<OrderItemModel> mapItems (List<OrderItem> items) {
        return items.stream().map(this::orderItemToOrderItemModel).toList();
    }

}