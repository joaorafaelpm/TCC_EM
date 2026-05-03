package com.pendezzapizza.pendezzapizza_api.api.v1.model.dto;


import com.pendezzapizza.pendezzapizza_api.core.validation.ValidationName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Classe de DTO para representação da entidade de <b>grupo</b>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupDTO {

    @Schema(example = "Gerente")
    @ValidationName
    private String name;
}
