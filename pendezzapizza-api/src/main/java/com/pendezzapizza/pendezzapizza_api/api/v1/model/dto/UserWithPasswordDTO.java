package com.pendezzapizza.pendezzapizza_api.api.v1.model.dto;

import com.pendezzapizza.pendezzapizza_api.core.validation.user.ValidPassword;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Classe de DTO para representação da entidade de <b>usuário com senha</b>
 */
@Getter
@Setter
@NoArgsConstructor
public class UserWithPasswordDTO extends UserDTO {

    @Schema(example = "senha", type = "string")
    @NotBlank
    @ValidPassword
    private String password;

    public UserWithPasswordDTO(String name, String email, String phone, String password ) {
        super(name, email , phone);
        this.password = password;
    }
}
