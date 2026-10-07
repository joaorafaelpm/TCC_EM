package com.pendezzapizza.pendezzapizza_api.domain.repository;

import com.pendezzapizza.pendezzapizza_api.domain.model.State;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Interface criada para fazer a ponte entre banco e entidade de <b>estado</b>
 *
 * <p>Toda query sql a mais está implementada dentro da função com seu código sql explicado  </p>
 */
@Repository
public interface StateRepository extends CustomJPARepository<State, UUID> {

//    Sobrescrevi para deixar paginado
    @Override
    Page<State> findAll (Pageable pageable) ;

    //    Pega a última data de atualização
    @Query("select max(s.updateDate) from State s")
    OffsetDateTime getLastStateUpdateDate();

//    Pega a última data de atualização por id
    @Query("select max(s.updateDate) from State s where s.id = :stateId")
    OffsetDateTime getLastStateUpdateDateById(UUID stateId);

}
