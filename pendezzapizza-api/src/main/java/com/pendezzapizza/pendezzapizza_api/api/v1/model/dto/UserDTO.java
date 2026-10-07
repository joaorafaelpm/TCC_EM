package com.pendezzapizza.pendezzapizza_api.api.v1.model.dto;

import com.pendezzapizza.pendezzapizza_api.core.validation.ValidationName;
import com.pendezzapizza.pendezzapizza_api.core.validation.restaurant_owner.ValidPhone;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Classe de DTO para representação da entidade de <b>usuário</b>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @Schema(example = "Rodrigo")
    @ValidationName
    private String name;

    @Schema(example = "rodrigo@gmail.com")
    @Email
    private String email;

    @Schema(example = "(19) 11111-1111")
    @ValidPhone
    @Nullable
    private String phone ;
}