package com.pendezzapizza.pendezzapizza_api.api.v1.model.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Classe de DTO para representação da entidade de <b>id de cidade</b>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CityIdDTO {

    @Schema(example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48")
    @NotNull
    private UUID id;
}
