package com.pendezzafood.pendezzapizza.domain.repositories;


import java.math.BigDecimal;
import java.util.List;

import com.pendezzafood.pendezzapizza.domain.models.Restaurante;

public interface RestauranteRepoQueries {

    List<Restaurante> find (String nome , BigDecimal taxaInicial , BigDecimal taxaFinal) ;

    List<Restaurante> findFreteGratisPorNome (String nome) ;

}
