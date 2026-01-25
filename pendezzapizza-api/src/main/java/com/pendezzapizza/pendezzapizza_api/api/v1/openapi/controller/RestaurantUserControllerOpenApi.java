package com.pendezzapizza.pendezzapizza_api.api.v1.openapi.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.UserModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "Restaurantes")
public interface RestaurantUserControllerOpenApi {

    @Operation(summary = "Lista os usuários responsáveis de um restaurante por id", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado", content = @Content(schema = @Schema(ref = "ApiError")))
    })
    CollectionModel<UserModel> list(@Parameter(example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", description = "Id do restaurante", required = true) UUID restaurantId);

    @Operation(summary = "Desassociação de um usuário a um restaurante por id", responses = {
            @ApiResponse(responseCode = "204", description = "Usuário desassociado"),
            @ApiResponse(responseCode = "404", description = "Restaurante ou usuário não encontrado", content = @Content(schema = @Schema(ref = "ApiError")))
    })
    ResponseEntity<Void> disassociate(@Parameter(example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", description = "Id do restaurante", required = true) UUID restaurantId,
                                      @Parameter(example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", description = "Id do usuário", required = true) UUID userId);

    @Operation(summary = "Associação de um usuário a um restaurante por id", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", description = "Restaurante ou usuário não encontrado", content = @Content(schema = @Schema(ref = "ApiError")))
    })
    ResponseEntity<Void> associate(@Parameter(example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", description = "Id do restaurante", required = true) UUID restaurantId,
                                   @Parameter(example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", description = "Id do usuário", required = true) UUID userId);
}