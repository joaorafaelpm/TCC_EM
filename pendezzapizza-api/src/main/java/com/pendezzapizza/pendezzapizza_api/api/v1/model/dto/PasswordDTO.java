package com.pendezzapizza.pendezzapizza_api.api.v1.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PasswordDTO {

    @Schema(example = "123", type = "string")
    @NotBlank
    private String currentPassword;

    @Schema(example = "abc", type = "string")
    @NotBlank
    private String newPassword;
}
