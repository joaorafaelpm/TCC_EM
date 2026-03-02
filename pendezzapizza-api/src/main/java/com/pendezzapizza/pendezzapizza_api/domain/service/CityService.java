package com.pendezzapizza.pendezzapizza_api.domain.service;

import com.pendezzapizza.pendezzapizza_api.domain.model.City;
import com.pendezzapizza.pendezzapizza_api.domain.model.State;
import com.pendezzapizza.pendezzapizza_api.domain.repository.CityRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CityService {

    private final CityRepository cityRepository;
    private final StateService stateService;

    @Cacheable("cities")
    public Page<City> findAll(Pageable pageable) {
        return cityRepository.findAll(pageable);
    }

    @Cacheable("city")
    public City findById(UUID id) {
        return cityRepository.findByIdOrThrowException(id);
    }

    @Cacheable("citiesLastUpdate")
    public OffsetDateTime getLastUpdateDate() {
        OffsetDateTime lastCityUpdateDate = cityRepository.getLastUpdateDate();
        OffsetDateTime lastStateUpdateDate = stateService.getLastUpdateDate();

        if (lastCityUpdateDate == null) return lastStateUpdateDate;
        if (lastStateUpdateDate == null) return lastCityUpdateDate;

        return lastCityUpdateDate.isAfter(lastStateUpdateDate)
                ? lastCityUpdateDate
                : lastStateUpdateDate;
    }

    @Cacheable("citiesLastUpdateById")
    public OffsetDateTime getLastUpdateDateById(UUID cityId) {
        City byId = findById(cityId);
        OffsetDateTime lastCityUpdateDate = cityRepository.getLastUpdateDateById(cityId);
        OffsetDateTime lastStateUpdateDate = stateService.getLastUpdateDateById(byId.getState().getId());

        if (lastCityUpdateDate == null) return lastStateUpdateDate;
        if (lastStateUpdateDate == null) return lastCityUpdateDate;

        return lastCityUpdateDate.isAfter(lastStateUpdateDate)
                ? lastCityUpdateDate
                : lastStateUpdateDate;
    }

    @Caching(evict = {
            @CacheEvict(value = "cities",            allEntries = true),
            @CacheEvict(value = "city",             key = "#city.id"),
            @CacheEvict(value = "citiesLastUpdate",  allEntries = true),
            @CacheEvict(value = "citiesLastUpdateById", key = "#city.id")
    })
    @Transactional
    public City save(City city) {
        UUID stateId = city.getState().getId();
        State state = stateService.findById(stateId);

        city.setState(state);
        return cityRepository.save(city);
    }

    @Caching(evict = {
            @CacheEvict(value = "cities",            allEntries = true),
            @CacheEvict(value = "city",             key = "#city.id"),
            @CacheEvict(value = "citiesLastUpdate",  allEntries = true),
            @CacheEvict(value = "citiesLastUpdateById", key = "#city.id")
    })
    @Transactional
    public City save(UUID id, City updatedCity) {
        City existingCity = findById(id);

        UUID stateId = updatedCity.getState().getId();
        State state = stateService.findById(stateId);

        existingCity.setState(state);

        return cityRepository.save(existingCity);
    }

    @Caching(evict = {
            @CacheEvict(value = "cities",            allEntries = true),
            @CacheEvict(value = "city",             key = "#city.id"),
            @CacheEvict(value = "citiesLastUpdate",  allEntries = true),
            @CacheEvict(value = "citiesLastUpdateById", key = "#city.id")
    })
    @Transactional
    public void delete(UUID id) {
        cityRepository.delete(findById(id));
        cityRepository.flush();
    }
}
