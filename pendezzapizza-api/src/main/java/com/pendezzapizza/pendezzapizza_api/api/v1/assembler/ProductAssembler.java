package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.PendezzaPizzaLinks;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.ProductMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.ProductModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Component
public class ProductAssembler extends RepresentationModelAssemblerSupport<Product, ProductModel> {

    @Autowired
    private PendezzaPizzaLinks links;

    @Autowired
    private ProductMapper productMapper;

    public ProductAssembler() {
        super(Product.class, ProductModel.class);
    }

    @Override
    public ProductModel toModel(Product entity) {
        ProductModel model = productMapper.toModel(entity);

        UUID restaurantId = entity.getRestaurant().getId();
        model.add(links.linkToRestaurantProducts(restaurantId, "products"));
        model.add(links.linkToProduct(restaurantId, entity.getId()));
        model.add(links.linkToProductPhoto(restaurantId, entity.getId(), "productPhoto"));

        return model;
    }

    public List<ProductModel> toCollection(Collection<Product> products) {
        return products.stream().map(this::toModel).toList();
    }
}
