package com.pendezzapizza.pendezzapizza_api.api.v1.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ProductModel {

    @Schema(example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48")
    private UUID id;

    @Schema(example = "Fettuccine Alfredo")
    private String name;

    @Schema(example = "Fettuccine artesanal com molho cremoso de manteiga e parmesão de 24 meses.")
    private String description;

    @Schema(example = "45.50")
    private BigDecimal price;

    @Schema(example = "true")
    private Boolean active;
}