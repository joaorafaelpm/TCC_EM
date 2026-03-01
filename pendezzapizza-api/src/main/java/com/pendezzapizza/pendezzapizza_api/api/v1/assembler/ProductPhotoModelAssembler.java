package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.ProductPhotoMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.ProductPhotoModel;
import com.pendezzapizza.pendezzapizza_api.core.security.PendezzaPizzaSecurity;
import com.pendezzapizza.pendezzapizza_api.domain.model.ProductPhoto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductPhotoModelAssembler{

    @Autowired
    private PendezzaPizzaSecurity pendezzaPizzaSecurity;

    @Autowired
    private ProductPhotoMapper productPhotoMapper;

    public ProductPhotoModel toModel(ProductPhoto productPhoto) {
        return productPhotoMapper.toModel(productPhoto);
    }
}