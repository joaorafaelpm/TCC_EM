package com.pendezzapizza.pendezzapizza_api.api.v1.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.PaymentMethodAssembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.PaymentMethodModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.Restaurant;
import com.pendezzapizza.pendezzapizza_api.domain.service.RestaurantService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/restaurants/{restaurantId}/payment-methods")
public class RestaurantPaymentMethodController {

    private RestaurantService restaurantService;
    private PaymentMethodAssembler paymentMethodAssembler;

    @GetMapping
    public CollectionModel<PaymentMethodModel> list(@PathVariable UUID restaurantId) {
        Restaurant restaurant = restaurantService.findById(restaurantId);
        return paymentMethodAssembler.toCollectionRefRestaurant(
                restaurantId,
                restaurant.getPaymentMethods()
        );
    }

    @PutMapping("/{paymentMethodId}")
    public ResponseEntity<Void> associate(
            @PathVariable UUID restaurantId,
            @PathVariable UUID paymentMethodId
    ) {
        restaurantService.associatePaymentMethod(restaurantId, paymentMethodId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{paymentMethodId}")
    public ResponseEntity<Void> disassociate(
            @PathVariable UUID restaurantId,
            @PathVariable UUID paymentMethodId
    ) {
        restaurantService.disassociatePaymentMethod(restaurantId, paymentMethodId);
        return ResponseEntity.noContent().build();
    }
}
