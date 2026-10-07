package com.pendezzapizza.pendezzapizza_api.api.v1.model.dto;

import com.pendezzapizza.pendezzapizza_api.core.validation.ValidationName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Classe de DTO para representação da entidade de <b>permissão</b>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PermissionDTO {

    @Schema(example = "EDITAR_COZINHAS")
    @NotBlank
    private String name;

    @Schema(example = "Permite editar cozinhas")
    @ValidationName
    private String description;
}
