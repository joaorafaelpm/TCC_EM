package com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.OrderSummaryModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.Order;
import org.mapstruct.Mapper;
import org.springframework.context.annotation.Bean;


@Mapper(componentModel = "spring")
public interface OrderSummaryMapper {

    @Bean
    OrderSummaryModel toModel(Order order);

}
