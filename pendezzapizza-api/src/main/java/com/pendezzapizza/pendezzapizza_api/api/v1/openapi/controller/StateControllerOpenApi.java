package com.pendezzapizza.pendezzapizza_api.api.v1.openapi.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.StateModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.StateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "Estados")
public interface StateControllerOpenApi {

    @Operation(summary = "Lista de estados")
    CollectionModel<StateModel> all();

    @Operation(summary = "Busca um estado por id", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", description = "Estado não encontrado", content = @Content(schema = @Schema(ref = "ApiError"))),
            @ApiResponse(responseCode = "400", description = "Erro no id do estado", content = @Content(schema = @Schema(ref = "ApiError")))
    })
    StateModel findById(@Parameter(description = "Id de um estado", example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", required = true) UUID stateId);

    @Operation(summary = "Cadastra um novo estado", responses = {
            @ApiResponse(responseCode = "201", description = "Estado cadastrado")
    })
    StateModel add(@RequestBody(description = "Representação de um novo estado", required = true) StateDTO stateDTO);

    @Operation(summary = "Atualiza as informações de um estado por id", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", description = "Estado não encontrado", content = @Content(schema = @Schema(ref = "ApiError"))),
            @ApiResponse(responseCode = "400", description = "Erro no id do estado", content = @Content(schema = @Schema(ref = "ApiError")))
    })
    StateModel save(@Parameter(description = "Id de um estado", example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", required = true) UUID stateId,
                    @RequestBody(description = "Representação de um estado com dados atualizados", required = true) StateDTO stateDTO);

    @Operation(summary = "Remove um estado por id", responses = {
            @ApiResponse(responseCode = "204", description = "Estado removido"),
            @ApiResponse(responseCode = "404", description = "Estado não encontrado", content = @Content(schema = @Schema(ref = "ApiError"))),
            @ApiResponse(responseCode = "409", description = "Estado não pode ser removido por que está sendo usado", content = @Content(schema = @Schema(ref = "ApiError")))
    })
    ResponseEntity<Void> remove(@Parameter(description = "Id de um estado", example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", required = true) UUID stateId);
}