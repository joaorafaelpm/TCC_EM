package com.pendezzapizza.pendezzapizza_api.api.v1.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.PaymentMethodModelAssembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.PaymentMethodModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.openapi.controller.RestaurantPaymentMethodControllerOpenApi;
import com.pendezzapizza.pendezzapizza_api.core.security.CheckSecurity;
import com.pendezzapizza.pendezzapizza_api.domain.model.PaymentMethod;
import com.pendezzapizza.pendezzapizza_api.domain.service.RestaurantService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@AllArgsConstructor
@RequestMapping(path ="/v1/restaurants/{restaurantId}/payment-methods", produces = MediaType.APPLICATION_JSON_VALUE)

public class RestaurantPaymentMethodController implements RestaurantPaymentMethodControllerOpenApi {

    private final RestaurantService restaurantService;
    private final PaymentMethodModelAssembler paymentMethodAssembler;

    @CheckSecurity.Restaurants.CanConsult
    @GetMapping
    public ResponseEntity<Page<PaymentMethodModel>> all(@PathVariable UUID restaurantId, Pageable pageable, ServletWebRequest request) {
        ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());
        String eTag = "0";
        OffsetDateTime lastUpdateDate = restaurantService.getLastUpdateDateById(restaurantId);
        if (lastUpdateDate != null) {
            eTag = String.valueOf(lastUpdateDate.toEpochSecond());
        }

        if (request.checkNotModified(eTag)) {
            return null;
        }

        Page<PaymentMethod> paymentMethodsByRestaurantId = restaurantService.findPaymentMethodsByRestaurantId(restaurantId , pageable);
        Page<PaymentMethodModel> collectionModel = paymentMethodsByRestaurantId.map(paymentMethodAssembler::toModel);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
                .eTag(eTag)
                .body(collectionModel);
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