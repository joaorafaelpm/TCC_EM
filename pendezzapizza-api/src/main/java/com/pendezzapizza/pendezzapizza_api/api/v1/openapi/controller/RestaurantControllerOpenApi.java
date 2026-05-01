package com.pendezzapizza.pendezzapizza_api.api.v1.openapi.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.RestaurantModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.RestaurantSummaryModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.RestaurantDTO;
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

@Tag(name = "Restaurantes")
public interface RestaurantControllerOpenApi {

    @Operation(summary = "Lista de restaurantes")
    ResponseEntity<Page<RestaurantSummaryModel>> list(
            @Parameter(required = false , description = "Filtra os restaurantes por nome") String restaurantName ,
            @Parameter(hidden = true) Pageable pageable ,
            @Parameter(hidden = true) ServletWebRequest request
            );

    @Operation(summary = "Busca um restaurante por id", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado", content = @Content(schema = @Schema(ref = "ApiError"))),
            @ApiResponse(responseCode = "400", description = "Erro no id do restaurante", content = @Content(schema = @Schema(ref = "ApiError")))
    })
    ResponseEntity<RestaurantModel> findById(@Parameter(description = "Id de um restaurante", example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", required = true) UUID restaurantId,
                                             @Parameter(hidden = true) ServletWebRequest request );

    @Operation(summary = "Cadastra um novo restaurante", responses = {
            @ApiResponse(responseCode = "201", description = "Restaurante criado")
    })
    RestaurantModel add(@RequestBody(description = "Representação de um novo restaurante", required = true) RestaurantDTO restaurantDTO);

    @Operation(summary = "Atualiza dados de um restaurante por id", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado", content = @Content(schema = @Schema(ref = "ApiError"))),
            @ApiResponse(responseCode = "400", description = "Erro no id do restaurante", content = @Content(schema = @Schema(ref = "ApiError")))
    })
    RestaurantModel save(@Parameter(description = "Id de um restaurante", example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", required = true) UUID restaurantId,
                         @RequestBody(description = "Representação de um restaurante com dados atualizados", required = true) RestaurantDTO restaurantDTO);

    @Operation(summary = "Ativa um restaurante por id", responses = {
            @ApiResponse(responseCode = "204", description = "Restaurante ativado"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado", content = @Content(schema = @Schema(ref = "ApiError")))
    })
    ResponseEntity<Void> activate(@Parameter(description = "Id de um restaurante", example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", required = true) UUID restaurantId);

    @Operation(summary = "Inativa um restaurante por id", responses = {
            @ApiResponse(responseCode = "204", description = "Restaurante inativado"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado", content = @Content(schema = @Schema(ref = "ApiError")))
    })
    ResponseEntity<Void> deactivate(@Parameter(description = "Id de um restaurante", example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", required = true) UUID restaurantId);

    @Operation(summary = "Abre um restaurante por id", responses = {
            @ApiResponse(responseCode = "204", description = "Restaurantes aberto"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado", content = @Content(schema = @Schema(ref = "ApiError")))
    })
    ResponseEntity<Void> open(@Parameter(description = "Id de um restaurante", example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", required = true) UUID restaurantId);

    @Operation(summary = "Fecha um restaurante por id", responses = {
            @ApiResponse(responseCode = "204", description = "Restaurantes fechado"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado", content = @Content(schema = @Schema(ref = "ApiError")))
    })
    ResponseEntity<Void> close(@Parameter(description = "Id de um restaurante", example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", required = true) UUID restaurantId);
}