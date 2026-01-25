package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.PendezzaLinks;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.RestaurantSummaryMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.RestaurantSummaryModel;
import com.pendezzapizza.pendezzapizza_api.core.security.PendezzaPizzaSecurity;
import com.pendezzapizza.pendezzapizza_api.domain.model.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class RestaurantSummaryModelAssembler extends RepresentationModelAssemblerSupport<Restaurant, RestaurantSummaryModel> {

    @Autowired
    private RestaurantSummaryMapper restaurantMapper;

    @Autowired
    private PendezzaLinks pendezzaLinks;

    @Autowired
    private PendezzaPizzaSecurity pendezzaPizzaSecurity;

    public RestaurantSummaryModelAssembler() {
        super(Restaurant.class, RestaurantSummaryModel.class);
    }

    @Override
    public RestaurantSummaryModel toModel(Restaurant entity) {
        RestaurantSummaryModel restaurantSummaryModel = restaurantMapper.toModel(entity);

        if (pendezzaPizzaSecurity.canConsultRestaurants()) {
            restaurantSummaryModel.add(pendezzaLinks.linkToRestaurant(restaurantSummaryModel.getId()));
            restaurantSummaryModel.add(pendezzaLinks.linkToRestaurants("restaurants"));
        }

        return restaurantSummaryModel;
    }

    @Override
    public CollectionModel<RestaurantSummaryModel> toCollectionModel(Iterable<? extends Restaurant> entities) {
        CollectionModel<RestaurantSummaryModel> collectionModel = super.toCollectionModel(entities);

        if (pendezzaPizzaSecurity.canConsultRestaurants()) {
            collectionModel.add(pendezzaLinks.linkToRestaurants("restaurants"));
        }

        return collectionModel;
    }
}