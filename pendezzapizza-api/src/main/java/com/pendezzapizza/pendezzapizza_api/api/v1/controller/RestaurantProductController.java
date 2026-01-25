package com.pendezzapizza.pendezzapizza_api.api.v1.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.ProductModelAssembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.disassambler.ProductDisassembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.ProductModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.ProductDTO;
import com.pendezzapizza.pendezzapizza_api.api.v1.openapi.controller.RestaurantProductControllerOpenApi;
import com.pendezzapizza.pendezzapizza_api.core.security.CheckSecurity;
import com.pendezzapizza.pendezzapizza_api.domain.model.Product;
import com.pendezzapizza.pendezzapizza_api.domain.service.ProductService;
import com.pendezzapizza.pendezzapizza_api.domain.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/restaurants/{restaurantId}/products")
@AllArgsConstructor
public class RestaurantProductController implements RestaurantProductControllerOpenApi {

    private final ProductService productService;
    private final RestaurantService restaurantService;
    private final ProductModelAssembler productAssembler;
    private final ProductDisassembler productDisassembler;

    @CheckSecurity.Restaurants.CanConsult
    @GetMapping
    public CollectionModel<ProductModel> findAllByRestaurant(@PathVariable UUID restaurantId, @RequestParam(required = false) Boolean includeInactives) {
        List<Product> products;
        if (includeInactives != null && includeInactives) {
            products = productService.findByRestaurant(restaurantService.findById(restaurantId));
        } else {
            products = productService.findActiveByRestaurant(restaurantService.findById(restaurantId));
        }
        return productAssembler.toCollectionModel(products);
    }

    @CheckSecurity.Restaurants.CanConsult
    @GetMapping("/{productId}")
    public ProductModel findById(@PathVariable UUID restaurantId, @PathVariable UUID productId) {
        return productAssembler.toModel(productService.findById(restaurantId, productId));
    }

    @CheckSecurity.Restaurants.CanManageOperation
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductModel add(@PathVariable UUID restaurantId, @RequestBody @Valid ProductDTO productDTO) {
        Product product = productDisassembler.productDTOToProduct(productDTO);
        return productAssembler.toModel(productService.save(restaurantId, product));
    }

    @CheckSecurity.Restaurants.CanManageOperation
    @PutMapping("/{productId}")
    public ProductModel save(@PathVariable UUID restaurantId, @PathVariable UUID productId, @RequestBody @Valid ProductDTO productDTO) {
        Product existingProduct = productService.findById(restaurantId, productId);
        productDisassembler.updateProductFromDto(productDTO, existingProduct);
        return productAssembler.toModel(productService.save(restaurantId, existingProduct));
    }
}