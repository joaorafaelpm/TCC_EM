package com.pendezzapizza.pendezzapizza_api.domain.repository;

import com.pendezzapizza.pendezzapizza_api.domain.model.State;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.UUID;

@Repository
public interface StateRepository extends CustomJPARepository<State, UUID> {

    @Override
    Page<State> findAll (Pageable pageable) ;

    @Query("select max(s.updateDate) from State s")
    OffsetDateTime getLastStateUpdateDate();

    @Query("select max(s.updateDate) from State s where s.id = :stateId")
    OffsetDateTime getLastStateUpdateDateById(UUID stateId);

}
