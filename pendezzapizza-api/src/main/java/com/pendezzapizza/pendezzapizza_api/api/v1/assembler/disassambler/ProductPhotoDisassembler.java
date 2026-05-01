package com.pendezzapizza.pendezzapizza_api.api.v1.assembler.disassambler;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.PhotoDTO;
import com.pendezzapizza.pendezzapizza_api.domain.model.ProductPhoto;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Disassembler da entidade de foto de produto usando a biblioteca Mapstruct, que requer a anotação {@code Mapper} para funcionar corretamente
 *
 * <p>Faço o mapeamento partindo da minha entidade de DTO para a minha original</p>
 * <p>Toda diferença entre o modelo e a entidade original é mapeada nos métodos da interface e para evitar warning é <b>necessário<b> mapear cada um dos parâmetros mesmo que seja o mesmo</p>
 */
@Mapper(componentModel = "spring")
public interface ProductPhotoDisassembler {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id" , ignore = true)
    @Mapping(target = "product" , ignore = true)
    @Mapping(target = "updateDate" , ignore = true)
    @Mapping(source = "file.contentType" , target = "contentType")
    @Mapping(source = "file.size" , target = "size")
    @Mapping(source = "file.originalFilename" , target = "fileName")
    ProductPhoto photoProductDTOToProductPhoto (PhotoDTO photoProductDTO) ;

}