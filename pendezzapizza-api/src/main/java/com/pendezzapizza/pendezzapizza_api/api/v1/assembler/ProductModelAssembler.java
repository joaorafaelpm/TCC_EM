package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.ProductMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.ProductModel;
import com.pendezzapizza.pendezzapizza_api.core.security.PendezzaPizzaSecurity;
import com.pendezzapizza.pendezzapizza_api.domain.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class ProductModelAssembler {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private PendezzaPizzaSecurity pendezzaPizzaSecurity;


    public ProductModel toModel(Product product) {

        return productMapper.toModel(product);
    }

    public Collection<ProductModel> toCollectionModel(Collection<Product> entities) {
        return entities.stream().map((this::toModel)).toList();

    }
}