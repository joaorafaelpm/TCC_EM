package com.pendezzapizza.pendezzapizza_api.api.v1.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
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
public class RestaurantDTO {

    @Schema(example = "Pizzaria fredbear")
    @NotBlank
    private String name;

    @Schema(example = "10.10", requiredMode = Schema.RequiredMode.REQUIRED)
    @PositiveOrZero
    private BigDecimal shippingFee;

    @Valid
    @NotNull
    private AddressDTO address;
}