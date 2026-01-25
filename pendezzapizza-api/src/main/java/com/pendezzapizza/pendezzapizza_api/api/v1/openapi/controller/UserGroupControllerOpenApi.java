package com.pendezzapizza.pendezzapizza_api.api.v1.openapi.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.GroupModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "Usuários")
public interface UserGroupControllerOpenApi {

    @Operation(summary = "Lista todas os grupos de um usuário por id", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content(schema = @Schema(ref = "ApiError")))
    })
    CollectionModel<GroupModel> getAllGroupsFromUser(@Parameter(description = "Id do usuário", example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", required = true) UUID userId);

    @Operation(summary = "Desassocia um grupo por id de um usuário por id", responses = {
            @ApiResponse(responseCode = "204", description = "Grupo desassociada"),
            @ApiResponse(responseCode = "404", description = "Grupo ou usuário não encontrado", content = @Content(schema = @Schema(ref = "ApiError")))
    })
    ResponseEntity<Void> disassociate(@Parameter(description = "Id do usuário", example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", required = true) UUID userId,
                                      @Parameter(description = "Id do grupo", example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", required = true) UUID groupId);

    @Operation(summary = "Associa um grupo por id de um usuário por id", responses = {
            @ApiResponse(responseCode = "204", description = "Grupo associado"),
            @ApiResponse(responseCode = "404", description = "Grupo ou usuário não encontrado", content = @Content(schema = @Schema(ref = "ApiError")))
    })
    ResponseEntity<Void> associate(@Parameter(description = "Id do usuário", example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", required = true) UUID userId,
                                   @Parameter(description = "Id do grupo", example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", required = true) UUID groupId);
}