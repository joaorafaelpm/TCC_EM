package com.pendezzapizza.pendezzapizza_api.domain.service;

import com.pendezzapizza.pendezzapizza_api.core.cache.CacheInvalidatorUtil;
import com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.StatesCacheEvict;
import com.pendezzapizza.pendezzapizza_api.domain.exception.EntityInUseException;
import com.pendezzapizza.pendezzapizza_api.domain.model.State;
import com.pendezzapizza.pendezzapizza_api.domain.repository.StateRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class StateService {

    private final StateRepository stateRepository;
    private CacheInvalidatorUtil cacheInvalidatorUtil;

    @Cacheable("states")
    public Page<State> findAll(Pageable pageable) {
        return stateRepository.findAll(pageable);
    }
    @Cacheable(value = "state", key = "#stateId")
    public State findById(UUID stateId) {
        return stateRepository.findByIdOrThrowException(stateId);
    }

    @Cacheable("statesLastUpdate")
    public OffsetDateTime getLastUpdateDate () {
        return stateRepository.getLastStateUpdateDate();
    }

    @Cacheable(value = "statesLastUpdateById", key = "#stateId")
    public OffsetDateTime getLastUpdateDateById (UUID stateId) {
        return stateRepository.getLastStateUpdateDateById(stateId);
    }


    @StatesCacheEvict
    @Transactional
    public State save(State state) {
        return stateRepository.save(state);
    }

    @StatesCacheEvict
    @Transactional
    public void delete(UUID stateId) {
        try {
            stateRepository.delete(findById(stateId));
            stateRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(stateId);
        }
    }
}
