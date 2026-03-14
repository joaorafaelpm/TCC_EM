package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.CityMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.CityModel;
import com.pendezzapizza.pendezzapizza_api.core.security.PendezzaPizzaSecurity;
import com.pendezzapizza.pendezzapizza_api.domain.model.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class CityModelAssembler {

    @Autowired
    private CityMapper cityMapper;

    @Autowired
    private PendezzaPizzaSecurity pendezzaPizzaSecurity;

    public CityModel toModel(City city) {
        return cityMapper.toModel(city);
    }

    public Collection<CityModel> toCollectionModel(Collection<City> entities) {
        return entities.stream().map((this::toModel)).toList();

    }
}