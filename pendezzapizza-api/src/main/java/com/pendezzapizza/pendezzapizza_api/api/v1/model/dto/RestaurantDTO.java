package com.pendezzapizza.pendezzapizza_api.api.v1.model.dto;

import com.pendezzapizza.pendezzapizza_api.core.validation.ValidationName;
import com.pendezzapizza.pendezzapizza_api.core.validation.restaurant_owner.ValidCpf;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Classe de DTO para representação da entidade de <b>restaurante</b>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantDTO {

    @Schema(example = "Pizzaria fredbear")
    @ValidationName
    private String name;

    @Schema(example = "380.225.620-41")
    @ValidCpf
    private String ownerCpf;

    @Schema(example = "Descrição do restaurante")
    private String description;

    @Schema(example = "19")
    @Positive
    private Integer averageDeliveryTimeMinutes;

    @Schema(example = "49.99")
    @Positive
    private BigDecimal minimumOrderValue;

    @Schema(example = "10.10", requiredMode = Schema.RequiredMode.REQUIRED)
    @PositiveOrZero
    private BigDecimal shippingFee;

    @Valid
    @NotNull
    private AddressDTO address;

}