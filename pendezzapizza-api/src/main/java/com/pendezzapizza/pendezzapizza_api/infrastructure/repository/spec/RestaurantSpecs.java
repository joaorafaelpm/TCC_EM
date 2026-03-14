package com.pendezzapizza.pendezzapizza_api.infrastructure.repository.spec;

import com.pendezzapizza.pendezzapizza_api.domain.model.Restaurant;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class RestaurantSpecs {

    public static Specification<Restaurant> withFreeShippingFee () {
        return (root,query,builder) ->
            builder.equal(root.get("shippingFee") , BigDecimal.ZERO) ;
    }

    public static Specification<Restaurant> withSimilarName (String name) {
        return (root,query,builder) ->
            builder.like(root.get("name") , "%" + name + "%") ;

    }

}
