package com.pendezzapizza.pendezzapizza_api.api.v1.model.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserWithPasswordDTO extends UserDTO {
    @NotBlank
    private String password;

    public UserWithPasswordDTO(String nome, String email , String password) {
        super(nome, email);
        this.password = password;
    }
}
