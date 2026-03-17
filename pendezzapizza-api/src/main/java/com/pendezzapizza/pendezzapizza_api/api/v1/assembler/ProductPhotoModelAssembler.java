package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.ProductPhotoMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.ProductPhotoModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.ProductPhoto;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProductPhotoModelAssembler{

    private ProductPhotoMapper productPhotoMapper;

    public ProductPhotoModel toModel(ProductPhoto productPhoto) {
        return productPhotoMapper.toModel(productPhoto);
    }
}