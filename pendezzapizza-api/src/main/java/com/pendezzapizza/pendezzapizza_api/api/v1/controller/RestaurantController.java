package com.pendezzapizza.pendezzapizza_api.api.v1.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.RestaurantModelAssembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.RestaurantSummaryModelAssembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.disassambler.RestaurantDisassembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.RestaurantModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.RestaurantSummaryModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.RestaurantDTO;
import com.pendezzapizza.pendezzapizza_api.api.v1.openapi.controller.RestaurantControllerOpenApi;
import com.pendezzapizza.pendezzapizza_api.core.security.CheckSecurity;
import com.pendezzapizza.pendezzapizza_api.domain.exception.BusinessException;
import com.pendezzapizza.pendezzapizza_api.domain.exception.RestaurantNotFoundException;
import com.pendezzapizza.pendezzapizza_api.domain.model.Restaurant;
import com.pendezzapizza.pendezzapizza_api.domain.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/restaurants")
public class RestaurantController implements RestaurantControllerOpenApi {

    private final RestaurantService restaurantService;
    private final RestaurantSummaryModelAssembler restaurantSummaryAssembler;
    private final RestaurantModelAssembler restaurantAssembler;
    private final RestaurantDisassembler restaurantDisassembler;

    @CheckSecurity.Restaurants.CanConsult
    @GetMapping
    public CollectionModel<RestaurantSummaryModel> list() {
        return restaurantSummaryAssembler.toCollectionModel(restaurantService.findAll());
    }

    @CheckSecurity.Restaurants.CanConsult
    @GetMapping("/{restaurantId}")
    public RestaurantModel findById(@PathVariable UUID restaurantId) {
        return restaurantAssembler.toModel(restaurantService.findById(restaurantId));
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

    @CheckSecurity.Restaurants.CanManageRegistration
    @PutMapping("/activations")
    public ResponseEntity<Void> activateMultiple(@RequestBody List<UUID> restaurantIds) {
        try {
            restaurantService.activate(restaurantIds);
            return ResponseEntity.noContent().build();
        } catch (RestaurantNotFoundException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @CheckSecurity.Restaurants.CanManageRegistration
    @DeleteMapping("/activations")
    public ResponseEntity<Void> deactivateMultiple(@RequestBody List<UUID> restaurantIds) {
        try {
            restaurantService.deactivate(restaurantIds);
            return ResponseEntity.noContent().build();
        } catch (RestaurantNotFoundException e) {
            throw new BusinessException(e.getMessage(), e);
        }
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