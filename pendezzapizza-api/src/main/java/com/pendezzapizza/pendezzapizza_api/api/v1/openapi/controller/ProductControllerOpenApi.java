package com.pendezzapizza.pendezzapizza_api.api.v1.openapi.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.ProductModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.ServletWebRequest;

@Tag(name = "Produtos")
public interface ProductControllerOpenApi {
    @Operation(summary = "Lista  de produtos")
    ResponseEntity<Page<ProductModel>> findAll(
            Boolean includeInactives,
            @Parameter(required = false, description = "Filtra um pedido pelo nome.") String productName ,
            @Parameter(hidden = true) Pageable pageable ,
            @Parameter(hidden = true) ServletWebRequest request);
}
