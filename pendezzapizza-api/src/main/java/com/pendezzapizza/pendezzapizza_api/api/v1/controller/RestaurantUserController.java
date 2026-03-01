package com.pendezzapizza.pendezzapizza_api.api.v1.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.UserModelAssembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.UserModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.openapi.controller.RestaurantUserControllerOpenApi;
import com.pendezzapizza.pendezzapizza_api.core.security.CheckSecurity;
import com.pendezzapizza.pendezzapizza_api.domain.model.Restaurant;
import com.pendezzapizza.pendezzapizza_api.domain.service.RestaurantService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping(path ="/v1/restaurants/{restaurantId}/responsible-users", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantUserController implements RestaurantUserControllerOpenApi {

    private final RestaurantService restaurantService;
    private final UserModelAssembler userModelAssembler;

    @CheckSecurity.Restaurants.CanManageRegistration
    @GetMapping
    public Collection<UserModel> list(@PathVariable UUID restaurantId) {
        Restaurant restaurant = restaurantService.findById(restaurantId);
        return userModelAssembler.toCollectionModel(restaurant.getResponsibleUsers());
    }

    @CheckSecurity.Restaurants.CanManageRegistration
    @PutMapping("/{userId}")
    public ResponseEntity<Void> associate(@PathVariable UUID restaurantId, @PathVariable UUID userId) {
        restaurantService.associateResponsibleUser(restaurantId, userId);
        return ResponseEntity.noContent().build();
    }

    @CheckSecurity.Restaurants.CanManageRegistration
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> disassociate(@PathVariable UUID restaurantId, @PathVariable UUID userId) {
        restaurantService.disassociateResponsibleUser(restaurantId, userId);
        return ResponseEntity.noContent().build();
    }
}