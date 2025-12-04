package com.pendezzapizza.pendezzapizza_api.api.v1.assembler.disassambler;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.DTO.ProductPhotoDTO;
import com.pendezzapizza.pendezzapizza_api.domain.model.ProductPhoto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.context.annotation.Bean;

@Mapper(componentModel = "spring")
public interface ProductPhotoDisassembler {

    @Bean
    @Mapping(target = "id" , ignore = true)
    @Mapping(target = "product" , ignore = true)
    @Mapping(source = "file.contentType" , target = "contentType")
    @Mapping(source = "file.size" , target = "size")
    @Mapping(source = "file.originalFilename" , target = "fileName")
    ProductPhoto photoProductDTOToProductPhoto (ProductPhotoDTO photoProductDTO) ;

}