/**
 * @summary     Representa o modelo de resposta de um usuário retornado pela API.
 *              Expõe apenas dados não sensíveis — credenciais e dados pessoais adicionais são omitidos.
 * @difficulty  Low
 * @depends-on  None
 */
package com.pendezzapizza.pendezzapizza_api.api.v1.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class UserModel {

    @Schema(example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48")
    private UUID id;

    @Schema(example = "Roberto")
    private String name;

    @Schema(example = "roberto@gmail.com")
    private String email;

    @Schema(example = "(19) 11111-1111")
    private String phone;
}