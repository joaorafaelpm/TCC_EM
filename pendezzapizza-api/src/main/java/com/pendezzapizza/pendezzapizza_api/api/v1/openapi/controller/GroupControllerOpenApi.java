package com.pendezzapizza.pendezzapizza_api.api.v1.openapi.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.GroupModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.GroupDTO;
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

@Tag(name = "Grupos")
public interface GroupControllerOpenApi {

    @Operation(summary = "Lista  de  Grupos")
    ResponseEntity<Page<GroupModel>> all(
            @Parameter(required = false, description = "Filtra um grupo pelo nome.") String groupName ,
            @Parameter(hidden = true) Pageable pageable,
            @Parameter(hidden = true) ServletWebRequest request);

    @Operation(summary = "Busca um Grupo por id", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404",
                    description = "Grupo não  encontrado",
                    content = @Content(schema = @Schema(ref = "ApiError")))})
    ResponseEntity<GroupModel> findById(
            @Parameter(description = "Id de um grupo", example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", required = true) UUID groupId,
            @Parameter(hidden = true) ServletWebRequest request);

    @Operation(summary = "Cadastro de um grupo", responses = {
            @ApiResponse(responseCode = "201", description = "Grupo cadastrado"),
            @ApiResponse(responseCode = "400",
                    description = "Nome do grupo inválido",
                    content = @Content(schema = @Schema(ref = "ApiError")))
    })
    GroupModel add(
            @RequestBody(description = "Representação de um novo grupo", required = true) GroupDTO groupDTO);

    @Operation(summary = "Atualiza um grupo por id", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404",
                    description = "Grupo não  encontrado",
                    content = @Content(schema = @Schema(ref = "ApiError"))),
            @ApiResponse(responseCode = "400",
                    description = "Nome do grupo inválido",
                    content = @Content(schema = @Schema(ref = "ApiError")))
    })
    GroupModel save(
            @Parameter(description = "Id de um grupo", example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", required = true) UUID groupId,
            @RequestBody(description = "Representação de um grupo com dados atualizados", required = true) GroupDTO groupDTO);

    @Operation(summary = "Remove um grupo por  id", responses = {
            @ApiResponse(responseCode = "204", description = "Grupo removido"),
            @ApiResponse(responseCode = "404",
                    description = "Grupo não  encontrado",
                    content = @Content(schema = @Schema(ref = "ApiError")))
    })
    ResponseEntity<Void> remove(
            @Parameter(description = "Id de um grupo", example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", required = true) UUID groupId);

}