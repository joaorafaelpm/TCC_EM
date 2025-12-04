package com.pendezzapizza.pendezzapizza_api.api.v1.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.RestaurantJustNameModelAssembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.RestaurantModelAssembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.RestaurantSummaryModelAssembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.disassambler.RestaurantDisassembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.DTO.RestaurantDTO;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.RestaurantJustNameModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.RestaurantModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.RestaurantSummaryModel;
import com.pendezzapizza.pendezzapizza_api.domain.exception.BusinessException;
import com.pendezzapizza.pendezzapizza_api.domain.exception.CityNotFoundException;
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
public class RestaurantController {

    private RestaurantService restaurantService;

    private RestaurantSummaryModelAssembler restaurantSummaryAssembler;
    private RestaurantJustNameModelAssembler restaurantJustNameModel;
    private RestaurantModelAssembler restaurantAssembler;

    private RestaurantDisassembler restaurantDisassembler;

    @GetMapping
    public CollectionModel<RestaurantSummaryModel> list() {
        return restaurantSummaryAssembler.toCollection(restaurantService.findAll());
    }

    @GetMapping(params = "projection=name-only")
    public CollectionModel<RestaurantJustNameModel> listNamesOnly() {
        return restaurantJustNameModel.toCollection(restaurantService.findAll());
    }

    @GetMapping("/{id}")
    public RestaurantModel getById(@PathVariable UUID id) {
        Restaurant restaurant = restaurantService.findById(id);
        return restaurantAssembler.toModel(restaurant);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestaurantModel add(@RequestBody @Valid RestaurantDTO restaurantDTO) {
        try {
            Restaurant restaurant =
                    restaurantService.save(restaurantDisassembler.restaurantDTOToRestaurant(restaurantDTO));

            return restaurantAssembler.toModel(restaurant);

        } catch (CityNotFoundException e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable UUID id,
            @RequestBody @Valid RestaurantDTO restaurantDTO
    ) {
        try {
            Restaurant oldRestaurant = restaurantService.findById(id);
            Restaurant updatedRestaurant = restaurantDisassembler.restaurantDTOToRestaurant(restaurantDTO);

            restaurantDisassembler.updateRestaurantFromDto(restaurantDTO, oldRestaurant);

            oldRestaurant.getAddress().setCity(updatedRestaurant.getAddress().getCity());

            return ResponseEntity.ok(
                    restaurantAssembler.toModel(restaurantService.save(oldRestaurant))
            );

        } catch (CityNotFoundException e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @PutMapping("/{id}/active")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> activate(@PathVariable UUID id) {
        restaurantService.activate(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/active")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        restaurantService.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/activations")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> activateMultiple(@RequestBody List<UUID> restaurantIds) {
        try {
            restaurantService.activate(restaurantIds);
            return ResponseEntity.noContent().build();
        } catch (RestaurantNotFoundException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @DeleteMapping("/activations")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deactivateMultiple(@RequestBody List<UUID> restaurantIds) {
        try {
            restaurantService.deactivate(restaurantIds);
            return ResponseEntity.noContent().build();
        } catch (RestaurantNotFoundException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @PutMapping("/{id}/open")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> open(@PathVariable UUID id) {
        restaurantService.open(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/close")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> close(@PathVariable UUID id) {
        restaurantService.close(id);
        return ResponseEntity.noContent().build();
    }
}
