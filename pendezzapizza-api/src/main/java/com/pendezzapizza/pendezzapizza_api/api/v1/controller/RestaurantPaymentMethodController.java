package com.pendezzapizza.pendezzapizza_api.api.v1.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.PaymentMethodModelAssembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.PaymentMethodModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.openapi.controller.RestaurantPaymentMethodControllerOpenApi;
import com.pendezzapizza.pendezzapizza_api.core.security.CheckSecurity;
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
public class RestaurantPaymentMethodController implements RestaurantPaymentMethodControllerOpenApi {

    private final RestaurantService restaurantService;
    private final PaymentMethodModelAssembler paymentMethodAssembler;

    @CheckSecurity.Restaurants.CanConsult
    @GetMapping
    public CollectionModel<PaymentMethodModel> all(@PathVariable UUID restaurantId) {
        Restaurant restaurant = restaurantService.findById(restaurantId);
        return paymentMethodAssembler.toCollectionRefRestaurant(restaurantId, restaurant.getPaymentMethods());
    }

    @CheckSecurity.Restaurants.CanManageOperation
    @DeleteMapping("/{paymentMethodId}")
    public ResponseEntity<Void> disassociate(@PathVariable UUID restaurantId, @PathVariable UUID paymentMethodId) {
        restaurantService.disassociatePaymentMethod(restaurantId, paymentMethodId);
        return ResponseEntity.noContent().build();
    }

    @CheckSecurity.Restaurants.CanManageOperation
    @PutMapping("/{paymentMethodId}")
    public ResponseEntity<Void> associate(@PathVariable UUID restaurantId, @PathVariable UUID paymentMethodId) {
        restaurantService.associatePaymentMethod(restaurantId, paymentMethodId);
        return ResponseEntity.noContent().build();
    }
}