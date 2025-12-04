package com.pendezzapizza.pendezzapizza_api.api.v1.model.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class PaymentMethodDTO {

    @NotBlank
    private String description ;
}
