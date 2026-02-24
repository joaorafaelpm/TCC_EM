package com.pendezzapizza.pendezzapizza_api.domain.service;

import com.pendezzapizza.pendezzapizza_api.domain.model.City;
import com.pendezzapizza.pendezzapizza_api.domain.model.State;
import com.pendezzapizza.pendezzapizza_api.domain.repository.CityRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CityService {

    private final CityRepository cityRepository;
    private final StateService stateService;

    public List<City> findAll() {
        return cityRepository.findAll();
    }

    public City findById(UUID id) {
        return cityRepository.findByIdOrThrowException(id);
    }

    @Transactional
    public City save(City city) {
        UUID stateId = city.getState().getId();
        State state = stateService.findById(stateId);

        city.setState(state);
        return cityRepository.save(city);
    }

    @Transactional
    public City save(UUID id, City updatedCity) {
        City existingCity = findById(id);

        UUID stateId = updatedCity.getState().getId();
        State state = stateService.findById(stateId);

        existingCity.setState(state);

        return cityRepository.save(existingCity);
    }

    @Transactional
    public void delete(UUID id) {
        cityRepository.delete(findById(id));
        cityRepository.flush();
    }
}
