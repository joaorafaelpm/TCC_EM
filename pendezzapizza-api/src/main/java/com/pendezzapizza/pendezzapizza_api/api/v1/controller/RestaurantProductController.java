package com.pendezzapizza.pendezzapizza_api.api.v1.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.ProductAssembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.disassambler.ProductDisassembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.DTO.ProductDTO;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.ProductModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.Product;
import com.pendezzapizza.pendezzapizza_api.domain.service.ProductService;
import com.pendezzapizza.pendezzapizza_api.domain.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/restaurants/{restaurantId}/products")
@AllArgsConstructor
public class RestaurantProductController {

    private ProductService productService;
    private RestaurantService restaurantService;

    private ProductAssembler productAssembler;
    private ProductDisassembler productDisassembler;

    @GetMapping
    public List<ProductModel> findAllByRestaurant(
            @PathVariable UUID restaurantId,
            @RequestParam(required = false) Boolean includeInactive
    ) {
        List<Product> products = productService.findActiveByRestaurant(
                restaurantService.findById(restaurantId)
        );

        if (includeInactive != null && includeInactive) {
            products = productService.findByRestaurant(
                    restaurantService.findById(restaurantId)
            );
        }

        return productAssembler.toCollection(products);
    }

    @GetMapping("/{productId}")
    public ProductModel findOne(
            @PathVariable UUID restaurantId,
            @PathVariable UUID productId
    ) {
        Product product = productService.findById(restaurantId, productId);
        return productAssembler.toModel(product);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductModel save(
            @PathVariable UUID restaurantId,
            @RequestBody @Valid ProductDTO productDTO
    ) {
        Product product = productDisassembler.produtoDTOToProduct(productDTO);
        productService.save(restaurantId, product);
        return productAssembler.toModel(product);
    }

    @PutMapping("/{productId}")
    public ProductModel update(
            @PathVariable UUID restaurantId,
            @PathVariable UUID productId,
            @RequestBody @Valid ProductDTO productDTO
    ) {
        Product oldProduct = productService.findById(restaurantId, productId);
        productDisassembler.updateProductFromDto(productDTO, oldProduct);
        return productAssembler.toModel(productService.save(restaurantId, oldProduct));
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable UUID restaurantId,
            @PathVariable UUID productId
    ) {
        productService.remove(restaurantId, productId);
    }
}
