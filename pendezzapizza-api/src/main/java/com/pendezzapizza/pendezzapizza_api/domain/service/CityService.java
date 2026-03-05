package com.pendezzapizza.pendezzapizza_api.domain.service;

import com.pendezzapizza.pendezzapizza_api.core.cache.CacheInvalidatorUtil;
import com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.CitiesCacheEvict;
import com.pendezzapizza.pendezzapizza_api.domain.model.City;
import com.pendezzapizza.pendezzapizza_api.domain.model.State;
import com.pendezzapizza.pendezzapizza_api.domain.repository.CityRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class CityService {

    private final CityRepository cityRepository;
    private final StateService stateService;
    private CacheInvalidatorUtil cacheInvalidatorUtil;

    @Cacheable("cities")
    public Page<City> findAll(Pageable pageable) {
        return cityRepository.findAll(pageable);
    }

    @Cacheable(value = "city", key = "#cityId")
    public City findById(UUID cityId) {
        return cityRepository.findByIdOrThrowException(cityId);
    }

    @Cacheable("citiesLastUpdate")
    public OffsetDateTime getLastUpdateDate() {
        return cityRepository.getLastUpdateDate();
    }

    @Cacheable(value = "citiesLastUpdateById", key = "#cityId")
    public OffsetDateTime getLastUpdateDateById(UUID cityId) {
        return cityRepository.getLastUpdateDateById(cityId);
    }

    @CitiesCacheEvict
    @Transactional
    public City save(City city) {
        UUID stateId = city.getState().getId();
        State state = stateService.findById(stateId);
        city.setState(state);
        return cityRepository.save(city);
    }

    @CitiesCacheEvict
    @Transactional
    public City save(UUID cityId, City updatedCity) {
        City existingCity = findById(cityId);
        UUID stateId = updatedCity.getState().getId();
        State state = stateService.findById(stateId);
        existingCity.setState(state);

        return cityRepository.save(existingCity);
    }

    @CitiesCacheEvict
    @Transactional
    public void delete(UUID cityId) {
        cityRepository.delete(findById(cityId));
        cityRepository.flush();
    }
}
