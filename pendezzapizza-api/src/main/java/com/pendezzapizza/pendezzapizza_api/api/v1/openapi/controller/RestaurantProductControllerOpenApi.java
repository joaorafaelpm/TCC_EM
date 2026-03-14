package com.pendezzapizza.pendezzapizza_api.api.v1.openapi.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.ProductModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.ProductDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.UUID;

@Tag(name = "Produtos")
public interface RestaurantProductControllerOpenApi {

    @Operation(summary = "Lista todos os produtos de um restaurante", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado", content = @Content(schema = @Schema(ref = "ApiError"))),
            @ApiResponse(responseCode = "400", description = "Erro no id do restaurante", content = @Content(schema = @Schema(ref = "ApiError")))
    })
    ResponseEntity<Page<ProductModel>> findAllByRestaurant(@Parameter(example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", description = "Id do restaurante", required = true) UUID restaurantId,
                                                           @Parameter(example = "false", description = "Incluir inativos", required = false) Boolean includeInactive,
                                                           @Parameter(hidden = true)Pageable pageable ,
                                                           @Parameter(hidden = true)ServletWebRequest request);

    @Operation(summary = "Pega um produto por id de um restaurante por id", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", description = "Restaurante ou produto não encontrado", content = @Content(schema = @Schema(ref = "ApiError"))),
            @ApiResponse(responseCode = "400", description = "Erro no id do restaurante ou do produto", content = @Content(schema = @Schema(ref = "ApiError")))
    })
    ResponseEntity<ProductModel> findById(@Parameter(example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", description = "Id do restaurante", required = true) UUID restaurantId,
                                          @Parameter(example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", description = "Id do produto", required = true) UUID productId,
                                          @Parameter(hidden = true)ServletWebRequest request);

    @Operation(summary = "Cadastra um produto a um restaurante por id", responses = {
            @ApiResponse(responseCode = "201", description = "Produto cadastrado"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado", content = @Content(schema = @Schema(ref = "ApiError")))
    })
    ProductModel add(@Parameter(example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", description = "Id do restaurante", required = true) UUID restaurantId,
                     @RequestBody(description = "Representação de um novo produto", required = true) ProductDTO productDTO);

    @Operation(summary = "Atualiza um produto por id de um restaurante por id", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", description = "Restaurante ou produto não encontrado", content = @Content(schema = @Schema(ref = "ApiError")))
    })
    ProductModel save(@Parameter(example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", description = "Id do restaurante", required = true) UUID restaurantId,
                        @Parameter(example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", description = "Id do produto", required = true) UUID productId,
                        @RequestBody(description = "Representação de um produto com dados atualizados", required = true) ProductDTO productDTO);
}