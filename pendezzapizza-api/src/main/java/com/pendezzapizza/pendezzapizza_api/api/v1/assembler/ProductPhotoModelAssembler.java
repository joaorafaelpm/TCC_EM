package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.PendezzaLinks;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.ProductPhotoMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.controller.RestaurantProductPhotoController;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.ProductPhotoModel;
import com.pendezzapizza.pendezzapizza_api.core.security.PendezzaPizzaSecurity;
import com.pendezzapizza.pendezzapizza_api.domain.model.ProductPhoto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProductPhotoModelAssembler extends RepresentationModelAssemblerSupport<ProductPhoto, ProductPhotoModel> {

    @Autowired
    private PendezzaLinks pendezzaLinks;

    @Autowired
    private PendezzaPizzaSecurity pendezzaPizzaSecurity;

    @Autowired
    private ProductPhotoMapper productPhotoMapper;

    public ProductPhotoModelAssembler() {
        // Geralmente o Controller responsável pela foto é o RestaurantProductPhotoController
        super(RestaurantProductPhotoController.class, ProductPhotoModel.class);
    }

    @Override
    public ProductPhotoModel toModel(ProductPhoto productPhoto) {
        ProductPhotoModel productPhotoModel = productPhotoMapper.toModel(productPhoto);

        // Navegação: Photo -> Product -> Restaurant -> ID
        UUID restaurantId = productPhoto.getProduct().getRestaurant().getId();
        UUID productId = productPhoto.getProduct().getId();

        if (pendezzaPizzaSecurity.canConsultRestaurants()) {
            // Link para o produto ao qual a foto pertence
            productPhotoModel.add(pendezzaLinks.linkToProduct(restaurantId, productId));

            // Link para a própria foto (self ou rel específico)
            productPhotoModel.add(pendezzaLinks.linkToProductPhoto(restaurantId, productId, "productPhoto"));
        }

        return productPhotoModel;
    }
}