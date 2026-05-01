package com.pendezzapizza.pendezzapizza_api.domain.repository;

import com.pendezzapizza.pendezzapizza_api.domain.model.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface criada para fazer a ponte entre banco e entidade de <b>cidade</b>
 *
 * <p>Toda query sql a mais está implementada dentro da função com seu código sql explicado  </p>
 */
@Repository
public interface CityRepository extends CustomJPARepository<City, UUID> {

    //    Feito para trazer o estado junto para reduzir o número de selects
    @Override
    @Query("SELECT c FROM City c JOIN FETCH c.state")
    Page<City> findAll(Pageable pageable);

//    Recebe uma cidade baseada no seu nome e no nome de seu estado
    @Query("SELECT c FROM City c JOIN FETCH c.state s WHERE c.name = :cityName and s.name = :stateName")
    Optional<City> findCityByNameAndStateName(String cityName , String stateName);

    // Ultima data de atualização de todas as cidades
    @Query("select max(c.updateDate) from City c")
    OffsetDateTime getLastUpdateDate();

    // Ultima data de atualização de cidade específica por id
    @Query("select max(c.updateDate) from City c where c.id = :cityId")
    OffsetDateTime getLastUpdateDateById(UUID cityId);

    // Ultima data de atualização de cidade específica por nome
    @Query("select max(c.updateDate) from City c where c.name = :cityName")
    OffsetDateTime getLastUpdateDateByName(String cityName);



}
