package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.ProductPhotoMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.PhotoModel;

import com.pendezzapizza.pendezzapizza_api.domain.model.ProductPhoto;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Component;

/**
 * Assembler da minha entidade de <b>foto de produto</b>
 *
 * <p>Essa é uma classe auxiliar que serve para usar o mapper de forma indireta</p>
 * <p>Opto por não usar o mapper direto para abrir a possibilidade de implementação de links (a gosto do freguês). Ou simplesmente adicionar lógica aqui dentro caso seja necessário </p>
 */
@Component
@AllArgsConstructor
public class ProductPhotoModelAssembler{

    private ProductPhotoMapper productPhotoMapper;

    public PhotoModel toModel(ProductPhoto productPhoto) {
        return productPhotoMapper.toModel(productPhoto);
    }
}