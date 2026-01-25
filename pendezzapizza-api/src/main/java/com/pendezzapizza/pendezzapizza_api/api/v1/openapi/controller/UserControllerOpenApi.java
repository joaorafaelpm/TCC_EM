package com.pendezzapizza.pendezzapizza_api.api.v1.openapi.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.UserModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.PasswordDTO;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.UserDTO;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.UserWithPasswordDTO;
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

@Tag(name = "Usuários")
public interface UserControllerOpenApi {

    @Operation(summary = "Busca todos os usuários")
    CollectionModel<UserModel> findAll();

    @Operation(summary = "Busca de um usuário por id", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content(schema = @Schema(ref = "ApiError"))),
            @ApiResponse(responseCode = "400", description = "Erro no id do usuário", content = @Content(schema = @Schema(ref = "ApiError")))
    })
    UserModel findById(@Parameter(example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", description = "Id do usuário", required = true) UUID userId);

    @Operation(summary = "Cadastra um novo usuário", responses = {
            @ApiResponse(responseCode = "201", description = "Usuário cadastrado")
    })
    UserModel add(@RequestBody(description = "Representação de um novo usuário", required = true) UserWithPasswordDTO userDTO);

    @Operation(summary = "Atualiza as informações do usuário por id", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content(schema = @Schema(ref = "ApiError")))
    })
    UserModel save(@Parameter(example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", description = "Id do usuário", required = true) UUID userId,
                     @RequestBody(description = "Representação de um usuário com dados atualizados", required = true) UserDTO userDTO);

    @Operation(summary = "Salva uma senha de um usuário por id", responses = {
            @ApiResponse(responseCode = "204", description = "Senha salva"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content(schema = @Schema(ref = "ApiError")))
    })
    ResponseEntity<Void> updatePassword(@Parameter(example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", description = "Id do usuário", required = true) UUID userId,
                                        @RequestBody(description = "Representação da senha de um usuário", required = true) PasswordDTO passwordDTO);
}