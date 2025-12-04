package com.pendezzapizza.pendezzapizza_api.domain.repository;

import com.pendezzapizza.pendezzapizza_api.domain.model.City;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface CityRepository extends CustomJPARepository<City, UUID> {

}
