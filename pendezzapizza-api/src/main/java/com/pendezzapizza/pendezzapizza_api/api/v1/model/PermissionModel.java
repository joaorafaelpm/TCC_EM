/**
 * @summary     Representa o modelo de resposta de uma permissão de acesso retornado pela API.
 *              Permissões são associadas a grupos de usuários para controle de autorização.
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
public class PermissionModel {

    @Schema(example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48")
    private UUID id;

    // Identificador funcional da permissão usado internamente para checagens de autorização (ex: EDITAR_COZINHAS)
    @Schema(example = "EDITAR_COZINHAS")
    private String name;

    // Texto legível por humanos, destinado a exibição em interfaces de administração
    @Schema(example = "Permite editar cozinhas")
    private String description;
}