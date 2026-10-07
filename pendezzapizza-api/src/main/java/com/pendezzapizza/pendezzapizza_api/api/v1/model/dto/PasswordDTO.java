package com.pendezzapizza.pendezzapizza_api.api.v1.model.dto;


import com.pendezzapizza.pendezzapizza_api.core.validation.user.ValidPassword;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Classe de DTO para representação da entidade de <b>senha</b>
 */
@Getter
@Setter
@NoArgsConstructor
public class PasswordDTO {

    @Schema(example = "senha", type = "string")
    @NotBlank
    @ValidPassword
    private String currentPassword;

    @Schema(example = "senha", type = "string")
    @NotBlank
    @ValidPassword
    private String newPassword;
}
