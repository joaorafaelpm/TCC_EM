package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.PendezzaPizzaLinks;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.RestaurantMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.RestaurantModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class RestaurantModelAssembler
        extends RepresentationModelAssemblerSupport<Restaurant, RestaurantModel> {

    @Autowired
    private RestaurantMapper mapper;

    @Autowired
    private PendezzaPizzaLinks links;

    public RestaurantModelAssembler() {
        super(Restaurant.class, RestaurantModel.class);
    }

    @Override
    public RestaurantModel toModel(Restaurant entity) {
        RestaurantModel model = mapper.toModel(entity);


        if (model.getAddress() != null) {
            model.getAddress().getCity().add(links.linkToCity(model.getAddress().getCity().getId()));
        }

        model.add(links.linkToRestaurant(model.getId()));
        model.add(links.linkToRestaurantProducts(model.getId(), "products"));
        model.add(links.linkToRestaurants("restaurants"));

        if (entity.canOpen()) {
            model.add(links.linkToRestaurantOpening(model.getId(), "open"));
        }
        if (entity.canClose()) {
            model.add(links.linkToRestaurantClosing(model.getId(), "close"));
        }
        if (entity.canActivate()) {
            model.add(links.linkToRestaurantActivation(model.getId(), "activate"));
        }
        if (entity.canDeactivate()) {
            model.add(links.linkToRestaurantInactivation(model.getId(), "deactivate"));
        }

        model.add(links.linkToRestaurantPaymentMethods(model.getId(), "payment-methods"));
        model.add(links.linkToRestaurantManagers(model.getId(), "responsible-users"));

        return model;
    }

    public CollectionModel<RestaurantModel> toCollection(Collection<Restaurant> restaurants) {
        List<RestaurantModel> list = restaurants.stream().map(this::toModel).toList();
        CollectionModel<RestaurantModel> collection = CollectionModel.of(list);

        collection.add(links.linkToRestaurants("restaurants"));

        return collection;
    }
}
