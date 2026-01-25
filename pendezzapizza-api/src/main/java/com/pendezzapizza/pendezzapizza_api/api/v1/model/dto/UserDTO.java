package com.pendezzapizza.pendezzapizza_api.api.v1.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @Schema(example = "Rodrigo")
    @NotBlank
    private String name;

    @Schema(example = "rodrigo@gmail.com")
    @NotBlank
    @Email
    private String email;
}