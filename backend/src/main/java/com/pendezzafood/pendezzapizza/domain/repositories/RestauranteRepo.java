package com.pendezzafood.pendezzapizza.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.pendezzafood.pendezzapizza.domain.models.Restaurante;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RestauranteRepo
        extends RestauranteRepoQueries ,
        JpaSpecificationExecutor<Restaurante> ,
        JpaRepository<Restaurante, UUID> {

    List<Restaurante> findByTaxaFreteBetween (BigDecimal taxaFreteMenor , BigDecimal taxaFreteMaior);

    Optional<Restaurante> findFirstByNomeContaining (String nome);

    List<Restaurante> findTop2ByNomeContaining (String nome) ;

}
