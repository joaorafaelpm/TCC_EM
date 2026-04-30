package com.pendezzapizza.pendezzapizza_api.domain.repository;

import com.pendezzapizza.pendezzapizza_api.domain.model.City;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface RestaurantOwnerProfileRepository extends CustomJPARepository<City, UUID> {

}
