package com.pendezzapizza.pendezzapizza_api.api.v1.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.UserModelAssembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.UserModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.openapi.controller.RestaurantUserControllerOpenApi;
import com.pendezzapizza.pendezzapizza_api.core.security.CheckSecurity;
import com.pendezzapizza.pendezzapizza_api.domain.model.User;
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
@RequestMapping(path ="/v1/restaurants/{restaurantId}/responsible-users", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantUserController implements RestaurantUserControllerOpenApi {

    private final RestaurantService restaurantService;
    private final UserModelAssembler userModelAssembler;

    @CheckSecurity.Restaurants.CanManageOperation
    @GetMapping
    public ResponseEntity<Page<UserModel>> list(
            @PathVariable UUID restaurantId,
            Pageable pageable,
            ServletWebRequest request) {

        ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());
        String eTag = "0";

        OffsetDateTime lastUpdateDate = restaurantService.getLastUpdateDateById(restaurantId);
        if (lastUpdateDate != null) {
            eTag = String.valueOf(lastUpdateDate.toEpochSecond());
        }
        if (request.checkNotModified(eTag)) {
            return null;
        }

        Page<User> responsibleUsers= restaurantService.findResponsibleUsersByRestaurantId(restaurantId , pageable);
        Page<UserModel> responsibleUsersModel = responsibleUsers.map(userModelAssembler::toModel);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
                .eTag(eTag)
                .body(responsibleUsersModel);
    }

    @CheckSecurity.Restaurants.CanManageOperation
    @PutMapping("/{userId}")
    public ResponseEntity<Void> associate(@PathVariable UUID restaurantId, @PathVariable UUID userId) {
        restaurantService.associateResponsibleUser(restaurantId, userId);
        return ResponseEntity.noContent().build();
    }

    @CheckSecurity.Restaurants.CanManageOperation
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> disassociate(@PathVariable UUID restaurantId, @PathVariable UUID userId) {
        restaurantService.disassociateResponsibleUser(restaurantId, userId);
        return ResponseEntity.noContent().build();
    }
}