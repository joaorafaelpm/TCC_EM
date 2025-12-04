package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.PendezzaPizzaLinks;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.RestaurantSummaryMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.RestaurantJustNameModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class RestaurantJustNameModelAssembler
        extends RepresentationModelAssemblerSupport<Restaurant, RestaurantJustNameModel> {

    @Autowired
    private RestaurantSummaryMapper mapper;

    @Autowired
    private PendezzaPizzaLinks links;

    public RestaurantJustNameModelAssembler() {
        super(Restaurant.class, RestaurantJustNameModel.class);
    }

    @Override
    public RestaurantJustNameModel toModel(Restaurant entity) {
        RestaurantJustNameModel model = mapper.toModelSummary(entity);

        model.add(links.linkToRestaurant(model.getId()));
        model.add(links.linkToRestaurants("restaurants"));

        return model;
    }

    public CollectionModel<RestaurantJustNameModel> toCollection(Collection<Restaurant> restaurants) {
        List<RestaurantJustNameModel> list = restaurants.stream().map(this::toModel).toList();
        CollectionModel<RestaurantJustNameModel> collection = CollectionModel.of(list);

        collection.add(links.linkToRestaurants("restaurants"));

        return collection;
    }
}
