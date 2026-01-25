package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.PendezzaLinks;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.ProductMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.controller.RestaurantProductController;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.ProductModel;
import com.pendezzapizza.pendezzapizza_api.core.security.PendezzaPizzaSecurity;
import com.pendezzapizza.pendezzapizza_api.domain.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProductModelAssembler extends RepresentationModelAssemblerSupport<Product, ProductModel> {

    @Autowired
    private PendezzaLinks pendezzaLinks;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private PendezzaPizzaSecurity pendezzaPizzaSecurity;

    public ProductModelAssembler() {
        super(RestaurantProductController.class, ProductModel.class);
    }

    @Override
    public ProductModel toModel(Product product) {
        ProductModel productModel = productMapper.toModel(product);

        UUID restaurantId = product.getRestaurant().getId();

        if (pendezzaPizzaSecurity.canConsultRestaurants()) {
            productModel.add(pendezzaLinks.linkToRestaurantProducts(restaurantId, "products"));
            productModel.add(pendezzaLinks.linkToProduct(restaurantId, product.getId()));
            productModel.add(pendezzaLinks.linkToProductPhoto(restaurantId, product.getId(), "productPhoto"));
        }

        return productModel;
    }

    // Como não havia lógica extra no seu original, usamos o padrão
    @Override
    public CollectionModel<ProductModel> toCollectionModel(Iterable<? extends Product> entities) {
        return super.toCollectionModel(entities);
    }
}