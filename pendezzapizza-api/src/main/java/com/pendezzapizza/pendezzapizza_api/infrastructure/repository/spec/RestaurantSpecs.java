package com.pendezzapizza.pendezzapizza_api.infrastructure.repository.spec;

import com.pendezzapizza.pendezzapizza_api.domain.model.Restaurant;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

/**
 * Specs da entidade de {@code Restaurant} para filtrar as pesquisas
 */
public class RestaurantSpecs {

//    Filtramos por pesquisa com frete gratis
    public static Specification<Restaurant> withFreeShippingFee () {
        return (root,query,builder) ->
            builder.equal(root.get("shippingFee") , BigDecimal.ZERO) ;
    }

//    Filtramos por pesquisa com nome
    public static Specification<Restaurant> withSimilarName (String name) {
        return (root,query,builder) ->
            builder.like(root.get("name") , "%" + name + "%") ;

    }

}
