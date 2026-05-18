package com.pendezzapizza.pendezzapizza_api.api.v1.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.ProductModelAssembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.ProductModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.openapi.controller.ProductControllerOpenApi;
import com.pendezzapizza.pendezzapizza_api.core.security.CheckSecurity;
import com.pendezzapizza.pendezzapizza_api.domain.model.Product;
import com.pendezzapizza.pendezzapizza_api.domain.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(path ="/v1/products", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ProductController implements ProductControllerOpenApi {

    private final ProductService productService;
    private final ProductModelAssembler productAssembler;

    @CheckSecurity.Restaurants.CanConsult
    @GetMapping
    public ResponseEntity<Page<ProductModel>> findAll(@RequestParam(required = false) Boolean includeInactives ,
                                                      @RequestParam(required = false) String productName ,
                                                                  Pageable pageable , ServletWebRequest request) {
        Page<Product> products;
        if (includeInactives != null && includeInactives) {
            products = productService.findAll(pageable);
        }if (productName != null) {
            products = productService.findAllByNameAndActives(productName , pageable);
        }
        else {
            products = productService.findAllActive(pageable);
        }

        ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());
        String eTag = "0";
        OffsetDateTime lastUpdateDate = productService.getAllLastUpdateDate();
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
    public ResponseEntity<ProductModel> findById(@PathVariable UUID productId ,
                                                          ServletWebRequest request) {
        ProductModel productsModel = productAssembler.toModel(productService.findByProductId(productId));
        ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());
        String eTag = "0";
        OffsetDateTime lastUpdateDate = productService.findLastUpdateDateById(productId);
        if (lastUpdateDate != null) {
            eTag = String.valueOf(lastUpdateDate.toEpochSecond());
        }

        if (request.checkNotModified(eTag)) {
            return null;
        }

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
                .eTag(eTag)
                .body(productsModel);
    }

}