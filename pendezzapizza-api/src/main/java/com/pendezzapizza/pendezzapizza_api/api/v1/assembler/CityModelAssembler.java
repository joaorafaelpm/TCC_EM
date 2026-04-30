package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.CityMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.CityModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.City;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@AllArgsConstructor
public class CityModelAssembler {

//    Mapeamento do modelo da cidade para o meu modelo
    private CityMapper cityMapper;

//    Classe para mapear
    public CityModel toModel(City city) {
        return cityMapper.toModel(city);
    }

//    Eu crio essa classe unico e exclusivamente para tratar um coletivo de modelo de cidade, isso serve para TODAS as classes dos assemblers
    public Collection<CityModel> toCollectionModel(Collection<City> entities) {
        return entities.stream().map((this::toModel)).toList();
    }
}