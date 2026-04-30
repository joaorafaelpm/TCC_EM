package com.pendezzapizza.pendezzapizza_api.api.v1.model.dto;

import com.pendezzapizza.pendezzapizza_api.core.validation.restaurant_owner.ValidCpf;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantOwnerProfileDTO {

    @NotNull
    private UserIdDTO userId;

    @ValidCpf
    private String cpf;

}