package com.pendezzapizza.pendezzapizza_api.domain.repository;

import com.pendezzapizza.pendezzapizza_api.domain.model.Restaurant;

import java.math.BigDecimal;
import java.util.List;

public interface RestaurantRepositoryQueries {

    List<Restaurant> find (String nome , BigDecimal taxaInicial , BigDecimal taxaFinal) ;

    List<Restaurant> findFreeShippingFeeByName (String nome) ;

}
