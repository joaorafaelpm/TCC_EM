package com.pendezzapizza.pendezzapizza_api.api.v1.model.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class OrderDTO {

    @Valid
    @NotNull
    private RestaurantIdDTO restaurantId;

    @Valid
    @NotNull
    private UserIdDTO clientId ;

    @Valid
    @NotNull
    private PaymentMethodIdDTO paymentMethodId ;

    @Valid
    @NotNull
    private AddressDTO deliveryAddress ;

    @Valid
    @Size(min = 1)
    @NotNull
    private List<OrderItemDTO> items;

}

