package com.pendezzapizza.pendezzapizza_api.api.v1.controller;


import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.CityModelAssembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.disassambler.CityDisassembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.CityModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.DTO.CityDTO;
import com.pendezzapizza.pendezzapizza_api.domain.exception.BusinessException;
import com.pendezzapizza.pendezzapizza_api.domain.exception.StateNotFoundException;
import com.pendezzapizza.pendezzapizza_api.domain.model.City;
import com.pendezzapizza.pendezzapizza_api.domain.repository.CityRepository;
import com.pendezzapizza.pendezzapizza_api.domain.service.CityService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/v1/cities", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class CityController {

    private CityRepository cityRepository;

    private CityService cityService;

    private CityModelAssembler cityAssembler;
    private CityDisassembler cityDisassembler;

    @GetMapping
    public CollectionModel<CityModel> getAll() {
        return cityAssembler.toCollection(cityRepository.findAll());
    }

    @GetMapping("/{id}")
    public CityModel getById(@PathVariable UUID id) {
        return cityAssembler.toModel(cityService.findById(id));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CityModel add(@RequestBody @Valid CityDTO cityDTO) {
        try {
            City city = cityDisassembler.cityDTOToCity(cityDTO);
            return cityAssembler.toModel(cityService.save(city));
        }
        catch (StateNotFoundException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @PutMapping("/{id}")
    public CityModel update(@PathVariable UUID id, @RequestBody @Valid CityDTO cityDTO) {
        City oldCity = cityService.findById(id);
        City newCity = cityDisassembler.cityDTOToCity(cityDTO);

        cityDisassembler.updateCityFromDto(cityDTO, oldCity);
        oldCity.setState(newCity.getState());

        return cityAssembler.toModel(cityService.save(id, oldCity));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        cityService.delete(id);
    }
}
