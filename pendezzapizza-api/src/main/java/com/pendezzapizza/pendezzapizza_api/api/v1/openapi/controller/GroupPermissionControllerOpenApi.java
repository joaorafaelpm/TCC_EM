package com.pendezzapizza.pendezzapizza_api.api.v1.openapi.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.PermissionModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.List;
import java.util.UUID;

@Tag(name = "Grupos")
public interface GroupPermissionControllerOpenApi {

    @Operation(summary = "Lista todas as permissões de um grupo por id", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", description = "Grupo não encontrado", content = @Content(schema = @Schema(ref = "ApiError")))
    })
    ResponseEntity<List<PermissionModel>> listPermissions(@Parameter(description = "Id do grupo", example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", required = true) UUID groupId ,
                                                          @Parameter(hidden = true) ServletWebRequest request);

    @Operation(summary = "Desassocia uma permissão por id de um grupo por id", responses = {
            @ApiResponse(responseCode = "204", description = "Permissão desassociada"),
            @ApiResponse(responseCode = "404", description = "Grupo ou permissão não encontrado", content = @Content(schema = @Schema(ref = "ApiError")))
    })
    ResponseEntity<Void> disassociatePermission(@Parameter(description = "Id do grupo", example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", required = true) UUID groupId,
                                                @Parameter(description = "Id da permissão", example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", required = true) UUID permissionId);

    @Operation(summary = "Associa uma permissão por id de um grupo por id", responses = {
            @ApiResponse(responseCode = "204", description = "Permissão associada"),
            @ApiResponse(responseCode = "404", description = "Grupo ou permissão não encontrado", content = @Content(schema = @Schema(ref = "ApiError")))
    })
    ResponseEntity<Void> associatePermission(@Parameter(description = "Id do grupo", example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", required = true) UUID groupId,
                                             @Parameter(description = "Id da permissão", example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", required = true) UUID permissionId);
}