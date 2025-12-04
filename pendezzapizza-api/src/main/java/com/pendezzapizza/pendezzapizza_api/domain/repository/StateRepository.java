package com.pendezzapizza.pendezzapizza_api.domain.repository;

import com.pendezzapizza.pendezzapizza_api.domain.model.State;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StateRepository extends CustomJPARepository<State, UUID> {

}
