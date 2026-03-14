package com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.ProductModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.Product;
import org.mapstruct.Mapper;
import org.springframework.context.annotation.Bean;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Bean
    ProductModel toModel(Product product);

}
