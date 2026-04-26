package com.pendezzapizza.pendezzapizza_api.domain.repository;

import com.pendezzapizza.pendezzapizza_api.domain.model.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface CityRepository extends CustomJPARepository<City, UUID> {

    @Override
    @Query("SELECT c FROM City c JOIN FETCH c.state")
    Page<City> findAll(Pageable pageable);

    @Query("SELECT c FROM City c JOIN FETCH c.state s WHERE c.name = :cityName and s.name = :stateName")
    Optional<City> findCityByNameAndStateName(String cityName , String stateName);

    // CityRepository
    @Query("select max(c.updateDate) from City c")
    OffsetDateTime getLastUpdateDate();

    @Query("select max(c.updateDate) from City c where c.id = :cityId")
    OffsetDateTime getLastUpdateDateById(UUID cityId);

    @Query("select max(c.updateDate) from City c where c.name = :cityName")
    OffsetDateTime getLastUpdateDateByName(String cityName);



}
