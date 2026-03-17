package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.ProductMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.ProductModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.Product;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@AllArgsConstructor
public class ProductModelAssembler {

    private ProductMapper productMapper;

    public ProductModel toModel(Product product) {

        return productMapper.toModel(product);
    }

    public Collection<ProductModel> toCollectionModel(Collection<Product> entities) {
        return entities.stream().map((this::toModel)).toList();

    }
}