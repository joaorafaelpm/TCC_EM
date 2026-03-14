package com.pendezzapizza.pendezzapizza_api.api.v1.assembler.disassambler;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.ProductDTO;
import com.pendezzapizza.pendezzapizza_api.domain.model.Product;
import org.mapstruct.*;
import org.springframework.context.annotation.Bean;

@Mapper(componentModel = "spring")
public interface ProductDisassembler {

    @Bean
    Product productDTOToProduct (ProductDTO productDTO) ;

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateProductFromDto(ProductDTO dto, @MappingTarget Product entity);

}