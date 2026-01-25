package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.PendezzaLinks;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.RestaurantMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.RestaurantModel;
import com.pendezzapizza.pendezzapizza_api.core.security.PendezzaPizzaSecurity;
import com.pendezzapizza.pendezzapizza_api.domain.model.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class RestaurantModelAssembler extends RepresentationModelAssemblerSupport<Restaurant, RestaurantModel> {

    @Autowired
    private RestaurantMapper restaurantMapper;

    @Autowired
    private PendezzaLinks pendezzaLinks;

    @Autowired
    private PendezzaPizzaSecurity pendezzaPizzaSecurity;

    public RestaurantModelAssembler() {
        super(Restaurant.class, RestaurantModel.class);
    }

    @Override
    public RestaurantModel toModel(Restaurant entity) {
        RestaurantModel restaurantModel = restaurantMapper.toModel(entity);

        if (pendezzaPizzaSecurity.canConsultCities()) {
            if (restaurantModel.getAddress() != null) {
                restaurantModel.getAddress().getCity().add(pendezzaLinks.
                        linkToCity(restaurantModel.getAddress().getCity().getId()));
            }
        }

        if (pendezzaPizzaSecurity.canConsultRestaurants()) {
            restaurantModel.add(pendezzaLinks.linkToRestaurant(restaurantModel.getId()));
            restaurantModel.add(pendezzaLinks.linkToRestaurants("restaurants"));
            restaurantModel.add(pendezzaLinks.linkToRestaurantProducts(restaurantModel.getId(), "products"));
        }

        if (pendezzaPizzaSecurity.canManageRestaurantOperation(restaurantModel.getId())) {
            if (entity.canOpen()) {
                restaurantModel.add(pendezzaLinks.linkToRestaurantOpening(restaurantModel.getId(), "open"));
            }
            if (entity.canClose()) {
                restaurantModel.add(pendezzaLinks.linkToRestaurantClosing(restaurantModel.getId(), "close"));
            }
        }

        if (pendezzaPizzaSecurity.canManageRestaurantRegistrations()) {
            if (entity.canActivate()) {
                restaurantModel.add(pendezzaLinks.linkToRestaurantActivation(restaurantModel.getId(), "activate"));
            }
            if (entity.canDeactivate()) {
                restaurantModel.add(pendezzaLinks.linkToRestaurantInactivation(restaurantModel.getId(), "inactivate"));
            }

            restaurantModel.add(pendezzaLinks.
                    linkToRestaurantManagers(restaurantModel.getId(), "managers")); // responsaveis -> managers
            restaurantModel.add(pendezzaLinks.
                    linkToRestaurantPaymentMethods(restaurantModel.getId(), "payment-methods"));
        }
        return restaurantModel;
    }

    @Override
    public CollectionModel<RestaurantModel> toCollectionModel(Iterable<? extends Restaurant> entities) {
        CollectionModel<RestaurantModel> collectionModel = super.toCollectionModel(entities);

        if (pendezzaPizzaSecurity.canConsultRestaurants()) {
            collectionModel.add(pendezzaLinks.linkToRestaurants("restaurants"));
        }

        return collectionModel;
    }
}