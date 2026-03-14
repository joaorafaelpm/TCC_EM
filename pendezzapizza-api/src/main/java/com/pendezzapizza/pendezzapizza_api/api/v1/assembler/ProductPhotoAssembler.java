package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.PendezzaPizzaLinks;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.ProductPhotoMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.ProductPhotoModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.ProductPhoto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProductPhotoAssembler extends RepresentationModelAssemblerSupport<ProductPhoto, ProductPhotoModel> {

    @Autowired
    private PendezzaPizzaLinks links;

    @Autowired
    private ProductPhotoMapper productPhotoMapper;

    public ProductPhotoAssembler() {
        super(ProductPhoto.class, ProductPhotoModel.class);
    }

    @Override
    public ProductPhotoModel toModel(ProductPhoto entity) {
        ProductPhotoModel model = productPhotoMapper.toModel(entity);
        UUID restaurantId = entity.getProduct().getRestaurant().getId();

        model.add(links.linkToProduct(restaurantId, entity.getId()));
        model.add(links.linkToProductPhoto(restaurantId, entity.getId(), "productPhoto"));

        return model;
    }
}
