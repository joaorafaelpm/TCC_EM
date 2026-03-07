package com.pendezzapizza.pendezzapizza_api.api.v1.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.RestaurantModelAssembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.disassambler.RestaurantDisassembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.RestaurantModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.RestaurantDTO;
import com.pendezzapizza.pendezzapizza_api.api.v1.openapi.controller.RestaurantControllerOpenApi;
import com.pendezzapizza.pendezzapizza_api.core.security.CheckSecurity;
import com.pendezzapizza.pendezzapizza_api.domain.model.Restaurant;
import com.pendezzapizza.pendezzapizza_api.domain.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
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
@RequestMapping(path ="/v1/restaurants", produces = MediaType.APPLICATION_JSON_VALUE)

public class RestaurantController implements RestaurantControllerOpenApi {

    private final RestaurantService restaurantService;
    private final RestaurantModelAssembler restaurantAssembler;
    private final RestaurantDisassembler restaurantDisassembler;

    @CheckSecurity.Restaurants.CanConsult
    @GetMapping
    public ResponseEntity<Page<RestaurantModel>> list(Pageable pageable , ServletWebRequest request) {
        ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());
        String eTag = "0";
        OffsetDateTime lastUpdateDate = restaurantService.getLastUpdateDate();
        if (lastUpdateDate != null) {
            eTag = String.valueOf(lastUpdateDate.toEpochSecond());
        }

        if (request.checkNotModified(eTag)) {
            return null;
        }

        Page<Restaurant> restaurants = restaurantService.findAll(pageable);
        Page<RestaurantModel> restaurantsModel = restaurants.map(restaurantAssembler::toModel);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
                .eTag(eTag)
                .body(restaurantsModel);
    }

    @CheckSecurity.Restaurants.CanConsult
    @GetMapping("/{restaurantId}")
    public ResponseEntity<RestaurantModel> findById(@PathVariable UUID restaurantId , ServletWebRequest request) {
        ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());
        String eTag = "0";
        OffsetDateTime lastUpdateDate = restaurantService.getLastUpdateDate();
        if (lastUpdateDate != null) {
            eTag = String.valueOf(lastUpdateDate.toEpochSecond());
        }

        if (request.checkNotModified(eTag)) {
            return null;
        }
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
                .eTag(eTag)
                .body(restaurantAssembler.toModel(restaurantService.findById(restaurantId)));
    }

    @CheckSecurity.Restaurants.CanManageRegistration
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public RestaurantModel add(@RequestBody @Valid RestaurantDTO restaurantDTO) {
        Restaurant restaurant = restaurantDisassembler.restaurantDTOToRestaurant(restaurantDTO);
        return restaurantAssembler.toModel(restaurantService.save(restaurant));
    }

    @CheckSecurity.Restaurants.CanManageRegistration
    @PutMapping("/{restaurantId}")
    public RestaurantModel save(@PathVariable UUID restaurantId, @RequestBody @Valid RestaurantDTO restaurantDTO) {
        Restaurant existingRestaurant = restaurantService.findById(restaurantId);
        restaurantDisassembler.updateRestaurantFromDto(restaurantDTO, existingRestaurant);
        return restaurantAssembler.toModel(restaurantService.save(existingRestaurant));
    }

    @CheckSecurity.Restaurants.CanManageRegistration
    @PutMapping("/{restaurantId}/active")
    public ResponseEntity<Void> activate(@PathVariable UUID restaurantId) {
        restaurantService.activate(restaurantId);
        return ResponseEntity.noContent().build();
    }

    @CheckSecurity.Restaurants.CanManageRegistration
    @DeleteMapping("/{restaurantId}/active")
    public ResponseEntity<Void> deactivate(@PathVariable UUID restaurantId) {
        restaurantService.deactivate(restaurantId);
        return ResponseEntity.noContent().build();
    }

    @CheckSecurity.Restaurants.CanManageOperation
    @PutMapping("/{restaurantId}/opening")
    public ResponseEntity<Void> open(@PathVariable UUID restaurantId) {
        restaurantService.open(restaurantId);
        return ResponseEntity.noContent().build();
    }

    @CheckSecurity.Restaurants.CanManageOperation
    @PutMapping("/{restaurantId}/closing")
    public ResponseEntity<Void> close(@PathVariable UUID restaurantId) {
        restaurantService.close(restaurantId);
        return ResponseEntity.noContent().build();
    }
}