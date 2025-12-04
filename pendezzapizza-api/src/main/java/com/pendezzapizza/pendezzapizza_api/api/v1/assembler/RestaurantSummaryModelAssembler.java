package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.PendezzaPizzaLinks;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.RestaurantSummaryMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.RestaurantSummaryModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class RestaurantSummaryModelAssembler
        extends RepresentationModelAssemblerSupport<Restaurant, RestaurantSummaryModel> {

    @Autowired
    private RestaurantSummaryMapper mapper;

    @Autowired
    private PendezzaPizzaLinks links;

    public RestaurantSummaryModelAssembler() {
        super(Restaurant.class, RestaurantSummaryModel.class);
    }

    @Override
    public RestaurantSummaryModel toModel(Restaurant entity) {
        RestaurantSummaryModel model = mapper.toModel(entity);

        model.add(links.linkToRestaurant(model.getId()));
        model.add(links.linkToRestaurants("restaurants"));

        return model;
    }

    public CollectionModel<RestaurantSummaryModel> toCollection(Collection<Restaurant> restaurants) {
        List<RestaurantSummaryModel> list = restaurants.stream().map(this::toModel).toList();
        CollectionModel<RestaurantSummaryModel> collection = CollectionModel.of(list);

        collection.add(links.linkToRestaurants("restaurants"));

        return collection;
    }
}
