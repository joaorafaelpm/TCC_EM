package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.ProductPhotoMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.PhotoModel;

import com.pendezzapizza.pendezzapizza_api.domain.model.ProductPhoto;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProductPhotoModelAssembler{

    private ProductPhotoMapper productPhotoMapper;

    public PhotoModel toModel(ProductPhoto productPhoto) {
        return productPhotoMapper.toModel(productPhoto);
    }
}