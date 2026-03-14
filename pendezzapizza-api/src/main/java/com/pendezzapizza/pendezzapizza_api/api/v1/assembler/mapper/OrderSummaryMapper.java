package com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.OrderSummaryModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.context.annotation.Bean;

@Mapper(componentModel = "spring")
public interface OrderSummaryMapper {

    @Mapping(source = "orderStatus", target = "status")
    @Bean
    OrderSummaryModel toModel(Order order);

}