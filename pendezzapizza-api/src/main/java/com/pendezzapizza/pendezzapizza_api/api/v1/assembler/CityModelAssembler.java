package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.PendezzaPizzaLinks;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.CityMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.controller.CityController;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.CityModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CityModelAssembler extends RepresentationModelAssemblerSupport<City, CityModel> {

    @Autowired
    private CityMapper cityMapper;

    @Autowired
    private PendezzaPizzaLinks links;

    public CityModelAssembler() {
        super(CityController.class, CityModel.class);
    }

    @Override
    public CityModel toModel(City city) {
        CityModel model = cityMapper.toModel(city);

        model.add(links.linkToCity(model.getId()));
        model.getState().add(links.linkToState(model.getState().getId()));
        model.add(links.linkToCities());

        return model;
    }

    public CollectionModel<CityModel> toCollection(List<City> cities) {
        var models = cities.stream().map(this::toModel).toList();
        CollectionModel<CityModel> collection = CollectionModel.of(models);
        collection.add(links.linkToCities("cities"));
        return collection;
    }
}
