package com.pendezzapizza.pendezzapizza_api.domain.service;

import com.pendezzapizza.pendezzapizza_api.domain.model.RestaurantOwnerProfile;
import com.pendezzapizza.pendezzapizza_api.domain.repository.RestaurantOwnerProfileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class RestaurantOwnerProfileService {

    private final RestaurantOwnerProfileRepository repository ;

    public RestaurantOwnerProfile save (RestaurantOwnerProfile restaurantOwnerProfile) {
        return repository.save(restaurantOwnerProfile);
    }

}
