package com.pendezzapizza.pendezzapizza_api.api.v1.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.RestaurantModelAssembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.RestaurantModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.openapi.controller.UserRestaurantsControllerOpenApi;
import com.pendezzapizza.pendezzapizza_api.core.security.CheckSecurity;
import com.pendezzapizza.pendezzapizza_api.domain.model.Restaurant;
import com.pendezzapizza.pendezzapizza_api.domain.service.RestaurantService;
import com.pendezzapizza.pendezzapizza_api.domain.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping(path ="/v1/users/{userId}/restaurants", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserRestaurantController implements UserRestaurantsControllerOpenApi {

    private final UserService userService;
    private final RestaurantService restaurantService;
    private final RestaurantModelAssembler restaurantModelAssembler;

    @CheckSecurity.Restaurants.CanManageRegistration
    @GetMapping
    public ResponseEntity<Page<RestaurantModel>> list(
            @PathVariable UUID userId,
            Pageable pageable,
            ServletWebRequest request) {

//        ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());
//        String eTag = "0";
//
//        OffsetDateTime lastUpdateDate = userService.getLastUpdateDateById(userId);
//        OffsetDateTime lastUpdateDateRestaurant = restaurantService.getLastUpdateDateById(userId);
//        if (lastUpdateDate != null) {
//            eTag = String.valueOf(lastUpdateDate.toEpochSecond());
//        }
//        if (request.checkNotModified(eTag)) {
//            return null;
//        }

        Page<Restaurant> responsibleUsers= userService.findRestaurantByUserId(userId , pageable);
        Page<RestaurantModel> responsibleUsersModel = responsibleUsers.map(restaurantModelAssembler::toModel);
//        return ResponseEntity.ok()
//                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
//                .eTag(eTag)
//                .body(responsibleUsersModel);
        return ResponseEntity.ok()
                .body(responsibleUsersModel);
    }

}