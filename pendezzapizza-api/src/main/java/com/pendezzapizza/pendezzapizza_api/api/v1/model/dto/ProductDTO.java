package com.pendezzapizza.pendezzapizza_api.api.v1.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    @Schema(example = "Porco com molho agridoce")
    @NotBlank
    private String name;

    @Schema(example = "Deliciosa carne suína ao molho especial")
    @NotBlank
    private String description;

    @Schema(example = "78.90", requiredMode = Schema.RequiredMode.REQUIRED)
    @PositiveOrZero
    private BigDecimal price;

    @Schema(example = "false")
    @NotNull
    private Boolean active = false;
}