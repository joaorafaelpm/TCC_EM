package com.pendezzapizza.pendezzapizza_api.api.v1.openapi.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.ProductPhotoModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.ProductPhotoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.context.request.ServletWebRequest;

import java.io.IOException;
import java.util.UUID;

@Tag(name = "Produtos")
public interface RestaurantProductPhotoControllerOpenApi {

    @Operation(summary = "Atualiza a foto do produto de um restaurante", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", description = "Restaurante ou produto não encontrado", content = @Content(schema = @Schema(ref = "ApiError")))
    })
    ProductPhotoModel updatePhoto(@Parameter(description = "Id  do restaurante", example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", required = true) UUID restaurantId,
                               @Parameter(description = "Id  do produto", example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", required = true) UUID productId,
                               @RequestBody(required = true) ProductPhotoDTO productPhotoDTO) throws IOException;

    @Operation(summary = "Remove uma foto de um produto por id de um restaurante por id", responses = {
            @ApiResponse(responseCode = "204", description = "Foto removida"),
            @ApiResponse(responseCode = "400", description = "Id do restaurante ou produto inválido", content = @Content(schema = @Schema(ref = "ApiError"))),
            @ApiResponse(responseCode = "404", description = "Restaurante ou produto não encontrado", content = @Content(schema = @Schema(ref = "ApiError")))
            })
    ResponseEntity<Void> removePhoto(@Parameter(description = "Id  do restaurante", example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", required = true) UUID restaurantId,
            @Parameter(description = "Id  do produto", example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", required = true) UUID productId);

    @Operation(summary = "Busca a foto de um produto de um  restaurante", responses = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ProductPhotoModel.class)),
                    @Content(mediaType = "image/jpeg", schema = @Schema(type = "string", format = "binary")),
                    @Content(mediaType = "image/png", schema = @Schema(type = "string", format = "binary"))
            }),
            @ApiResponse(responseCode = "404", description = "Restaurante ou produto não encontrado", content = @Content(schema = @Schema(ref = "ApiError")))
    })
    ResponseEntity<ProductPhotoModel> findPhoto(@Parameter(description = "Id  do restaurante", example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", required = true) UUID restaurantId,
                               @Parameter(description = "Id  do produto", example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", required = true) UUID productId,
                               @Parameter(hidden = true) ServletWebRequest request);

    @Operation(hidden = true)
    ResponseEntity<?> servePhoto(UUID restaurantId, UUID productId, String acceptHeader) throws HttpMediaTypeNotAcceptableException;
}