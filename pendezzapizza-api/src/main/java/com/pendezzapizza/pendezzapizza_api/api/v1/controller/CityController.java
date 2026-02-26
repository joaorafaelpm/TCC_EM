package com.pendezzapizza.pendezzapizza_api.api.v1.controller;

import com.pendezzapizza.pendezzapizza_api.api.ResourceUriHelper;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.CityModelAssembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.disassambler.CityDisassembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.CityModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.CityDTO;
import com.pendezzapizza.pendezzapizza_api.api.v1.openapi.controller.CityControllerOpenApi;
import com.pendezzapizza.pendezzapizza_api.core.security.CheckSecurity;
import com.pendezzapizza.pendezzapizza_api.domain.exception.BusinessException;
import com.pendezzapizza.pendezzapizza_api.domain.exception.StateNotFoundException;
import com.pendezzapizza.pendezzapizza_api.domain.model.City;
import com.pendezzapizza.pendezzapizza_api.domain.repository.CityRepository;
import com.pendezzapizza.pendezzapizza_api.domain.service.CityService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/v1/cities", produces = MediaType.APPLICATION_JSON_VALUE)
public class CityController implements CityControllerOpenApi {

    private final CityRepository cityRepository;
    private final CityService cityService;
    private final CityModelAssembler cityAssembler;
    private final CityDisassembler cityDisassembler;

    private final PagedResourcesAssembler<City> pagedResourcesAssembler;


    @CheckSecurity.Cities.CanConsult
    @GetMapping
    public PagedModel<CityModel> all(Pageable pageable) {
        Page<City> findAll = cityService.findAll(pageable);
        return pagedResourcesAssembler.toModel(findAll , cityAssembler);
    }

    @CheckSecurity.Cities.CanConsult
    @GetMapping(value = "/{cityId}")
    public CityModel findById(@PathVariable UUID cityId) {
        return cityAssembler.toModel(cityService.findById(cityId));
    }

    @CheckSecurity.Cities.CanEdit
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CityModel add(@RequestBody @Valid CityDTO cityDTO) {
        try {
            City city = cityDisassembler.cityDTOToCity(cityDTO);
            CityModel cityModel = cityAssembler.toModel(cityService.save(city));

            ResourceUriHelper.addUriResponseHeader(cityModel.getId());

            return cityModel;
        } catch (StateNotFoundException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @CheckSecurity.Cities.CanEdit
    @PutMapping(value = "/{cityId}")
    public CityModel save(@PathVariable UUID cityId, @RequestBody @Valid CityDTO cityDTO) {
        try {
            City existingCity = cityService.findById(cityId);

            cityDisassembler.updateCityFromDto(cityDTO, existingCity);

            return cityAssembler.toModel(cityService.save(existingCity));
        } catch (StateNotFoundException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @CheckSecurity.Cities.CanEdit
    @DeleteMapping("/{cityId}")
    public ResponseEntity<Void> remove(@PathVariable UUID cityId) {
        cityService.delete(cityId);
        return ResponseEntity.noContent().build();
    }
}