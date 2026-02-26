package com.pendezzapizza.pendezzapizza_api.api.v1.openapi.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.CityModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.CityDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "Cidades")
public interface CityControllerOpenApi {

    @Operation(summary = "Lista de cidades")
    PagedModel<CityModel> all( @Parameter(hidden = true) Pageable pageable);

    @Operation(summary = "Busca uma Cidade por id", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404",
                    description = "Cidade não  encontrada",
                    content = @Content(schema = @Schema(ref = "ApiError"))),
            @ApiResponse(responseCode = "400",
                    description = "Id da cidade inválido",
                    content = @Content(schema = @Schema(ref = "ApiError")))
    })
    CityModel findById(
            @Parameter(description = "Id de uma cidade", example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", required = true) UUID cityId);

    @Operation(summary = "Cadastra uma Cidade",
            description = "Cadastro de uma Cidade, necesita de um Estado e nome válido")
    CityModel add(
            @RequestBody(description = "Representação de uma nova cidade", required = true) CityDTO cityDTO);

    @Operation(summary = "Atualiza uma Cidade por Id", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404",
                    description = "Cidade não  encontrada",
                    content = @Content(schema = @Schema(ref = "ApiError"))),
            @ApiResponse(responseCode = "400",
                    description = "Id da cidade inválido",
                    content = @Content(schema = @Schema(ref = "ApiError")))
    })
    CityModel save(@Parameter(description = "Id de uma cidade", example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", required = true) UUID cityId,
                   @RequestBody(description = "Representação de uma cidade com dados atualizados", required = true) CityDTO cityDTO);

    @Operation(summary = "Remove uma Cidade  por Id", responses = {
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "404",
                    description = "Cidade não  encontrada",
                    content = @Content(schema = @Schema(ref = "ApiError"))),
            @ApiResponse(responseCode = "400",
                    description = "Id da cidade inválido",
                    content = @Content(schema = @Schema(ref = "ApiError")))
    })
    ResponseEntity<Void> remove(@Parameter(description = "Id de uma cidade", example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", required = true) UUID cityId);

}