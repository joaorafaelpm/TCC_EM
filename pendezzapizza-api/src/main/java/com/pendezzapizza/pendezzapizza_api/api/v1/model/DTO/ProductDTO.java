package com.pendezzapizza.pendezzapizza_api.api.v1.model.DTO;

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

    @NotBlank
    private String name ;
    @NotBlank
    private String description ;

    @PositiveOrZero
    @NotNull
    private BigDecimal price ;

    @NotNull
    private Boolean active = false;

}
