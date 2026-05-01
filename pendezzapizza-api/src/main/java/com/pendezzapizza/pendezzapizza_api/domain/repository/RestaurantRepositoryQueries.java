package com.pendezzapizza.pendezzapizza_api.domain.repository;

import com.pendezzapizza.pendezzapizza_api.domain.model.Restaurant;
import com.pendezzapizza.pendezzapizza_api.domain.model.RestaurantPhoto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Interface criada para fazer algumas funções extras da entidade de <b>restaurante</b>
 *
 * <p>Toda query sql a mais está implementada dentro da função com seu código sql explicado em sua implementação</p>
 */
public interface RestaurantRepositoryQueries {

    List<Restaurant> find (String nome , BigDecimal taxaInicial , BigDecimal taxaFinal) ;

    List<Restaurant> findFreeShippingFeeByName (String nome) ;

    RestaurantPhoto savePhoto (RestaurantPhoto foto) ;
    void deletePhoto(RestaurantPhoto foto) ;


}
