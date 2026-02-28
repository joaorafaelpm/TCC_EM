package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.PendezzaLinks;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.CityMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.controller.CityController;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.CityModel;
import com.pendezzapizza.pendezzapizza_api.core.security.PendezzaPizzaSecurity;
import com.pendezzapizza.pendezzapizza_api.domain.model.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class CityModelAssembler extends RepresentationModelAssemblerSupport<City, CityModel> {

    @Autowired
    private CityMapper cityMapper;

    @Autowired
    private PendezzaLinks pendezzaLinks;

    @Autowired
    private PendezzaPizzaSecurity pendezzaPizzaSecurity;

    public CityModelAssembler() {
        super(CityController.class, CityModel.class);
    }

    @Override
    public CityModel toModel(City city) {
        CityModel cityModel = cityMapper.toModel(city);

        if (pendezzaPizzaSecurity.canConsultCities()) {
            cityModel.add(pendezzaLinks.linkToCity(cityModel.getId()));
            cityModel.add(pendezzaLinks.linkToCities());
        }

        if (pendezzaPizzaSecurity.canConsultStates()) {
            cityModel.getState().add(pendezzaLinks.
                    linkToState(cityModel.getState().getId()));
        }

        return cityModel;
    }
    public CityModel toModel(CityModel cityModel) {

        if (pendezzaPizzaSecurity.canConsultCities()) {
            cityModel.add(pendezzaLinks.linkToCity(cityModel.getId()));
            cityModel.add(pendezzaLinks.linkToCities());
        }

        if (pendezzaPizzaSecurity.canConsultStates()) {
            cityModel.getState().add(pendezzaLinks.
                    linkToState(cityModel.getState().getId()));
        }

        return cityModel;
    }

    @Override
    public CollectionModel<CityModel> toCollectionModel(Iterable<? extends City> entities) {
        CollectionModel<CityModel> cityCollectionModel = super.toCollectionModel(entities);

        if (pendezzaPizzaSecurity.canConsultCities()) {
            cityCollectionModel.add(pendezzaLinks.linkToCities("cities"));
        }

        return cityCollectionModel;
    }
}