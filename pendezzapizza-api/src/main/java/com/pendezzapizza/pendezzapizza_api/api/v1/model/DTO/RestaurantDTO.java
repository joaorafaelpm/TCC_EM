package com.pendezzapizza.pendezzapizza_api.api.v1.model.DTO;


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

    @NotBlank
    private String name  ;

    @PositiveOrZero
    @NotNull
    private BigDecimal shippingFee  ;

    @Valid
    @NotNull
    private AddressDTO address;

}
