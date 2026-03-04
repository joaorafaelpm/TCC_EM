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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(path ="/v1/restaurants/{restaurantId}/products", produces = MediaType.APPLICATION_JSON_VALUE)

@AllArgsConstructor
public class RestaurantProductController implements RestaurantProductControllerOpenApi {

    private final ProductService productService;
    private final RestaurantService restaurantService;
    private final ProductModelAssembler productAssembler;
    private final ProductDisassembler productDisassembler;

    @CheckSecurity.Restaurants.CanConsult
    @GetMapping
    public ResponseEntity<Page<ProductModel>> findAllByRestaurant(@PathVariable UUID restaurantId, @RequestParam(required = false) Boolean includeInactives ,
                                                                  Pageable pageable , ServletWebRequest request) {
        Page<Product> products;
        if (includeInactives != null && includeInactives) {
            products = productService.findByRestaurant(restaurantService.findById(restaurantId), pageable);
        } else {
            products = productService.findActiveByRestaurant(restaurantService.findById(restaurantId), pageable);
        }

        ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());
        String eTag = "0";
        OffsetDateTime lastUpdateDate = productService.findLastUpdateDateAndActivesByRestaurantId(restaurantId);
        if (lastUpdateDate != null) {
            eTag = String.valueOf(lastUpdateDate.toEpochSecond());
        }

        if (request.checkNotModified(eTag)) {
            return null;
        }

        Page<ProductModel> productsModel = products.map(productAssembler::toModel);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
                .eTag(eTag)
                .body(productsModel);
    }

    @CheckSecurity.Restaurants.CanConsult
    @GetMapping("/{productId}")
    public ResponseEntity<ProductModel> findById(@PathVariable UUID restaurantId,
                                                 @PathVariable UUID productId,
                                                 ServletWebRequest request) {
        ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());
        String eTag = "0";
        OffsetDateTime lastUpdateDate = productService.findLastUpdateDateAndActivesByRestaurantId(restaurantId);
        if (lastUpdateDate != null) {
            eTag = String.valueOf(lastUpdateDate.toEpochSecond());
        }

        if (request.checkNotModified(eTag)) {
            return null;
        }
        ProductModel model = productAssembler.toModel(productService.findById(restaurantId, productId));

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
                .eTag(eTag)
                .body(model);
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
    @CheckSecurity.Restaurants.CanManageOperation
    @PutMapping("/{productId}/active")
    public ResponseEntity<Void> activeProduct(@PathVariable UUID restaurantId, @PathVariable UUID productId) {
        productService.active(
                restaurantId, productId
        );
        return ResponseEntity.noContent().build();
    }
    @CheckSecurity.Restaurants.CanManageOperation
    @DeleteMapping("/{productId}/active")
    public ResponseEntity<Void> deactivateProduct(@PathVariable UUID restaurantId, @PathVariable UUID productId) {
        productService.deactivate(
                restaurantId, productId
        );
        return ResponseEntity.noContent().build();
    }
}