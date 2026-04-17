package com.pendezzapizza.pendezzapizza_api.api.v1.assembler.disassambler;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.PhotoDTO;
import com.pendezzapizza.pendezzapizza_api.domain.model.RestaurantPhoto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface RestaurantPhotoDisassembler {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id" , ignore = true)
    @Mapping(target = "restaurant" , ignore = true)
//    @Mapping(target = "updateDate" , ignore = true)
    @Mapping(source = "file.contentType" , target = "contentType")
    @Mapping(source = "file.size" , target = "size")
    @Mapping(source = "file.originalFilename" , target = "fileName")
    RestaurantPhoto photoRestaurantDTOToRestaurantPhoto (PhotoDTO photoRestaurantDTO) ;

}