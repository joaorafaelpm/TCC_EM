package com.pendezzapizza.pendezzapizza_api.domain.service;

import com.pendezzapizza.pendezzapizza_api.domain.exception.EntityInUseException;
import com.pendezzapizza.pendezzapizza_api.domain.model.State;
import com.pendezzapizza.pendezzapizza_api.domain.repository.StateRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class StateService {

    private final StateRepository stateRepository;
    private StringRedisTemplate redisTemplate;

    @Cacheable("states")
    public Page<State> findAll(Pageable pageable) {
        return stateRepository.findAll(pageable);
    }
    @Cacheable(value = "state", key = "#id")
    public State findById(UUID id) {
        return stateRepository.findByIdOrThrowException(id);
    }

    @Cacheable("statesLastUpdate")
    public OffsetDateTime getLastUpdateDate () {
        return stateRepository.getLastStateUpdateDate();
    }
    @Cacheable(value = "statesLastUpdateById", key = "#id")
    public OffsetDateTime getLastUpdateDateById (UUID id) {
        return stateRepository.getLastStateUpdateDateById(id);
    }


    @Caching(evict = {
            @CacheEvict(value = "states",            allEntries = true),
            @CacheEvict(value = "state",             key = "#state.id"),
            @CacheEvict(value = "statesLastUpdate",  allEntries = true),
            @CacheEvict(value = "statesLastUpdateById", key = "#state.id")
    })
    @Transactional
    public State save(State state) {

        redisTemplate.convertAndSend("cache:invalidate", "states");
        redisTemplate.convertAndSend("cache:invalidate", "state");
        redisTemplate.convertAndSend("cache:invalidate", "statesLastUpdate");
        redisTemplate.convertAndSend("cache:invalidate", "statesLastUpdateById");

        return stateRepository.save(state);
    }

    @Caching(evict = {
            @CacheEvict(value = "states",            allEntries = true),
            @CacheEvict(value = "state",             key = "#id"),
            @CacheEvict(value = "statesLastUpdate",  allEntries = true),
            @CacheEvict(value = "statesLastUpdateById", key = "#id")
    })
    @Transactional
    public void delete(UUID id) {
        try {

            redisTemplate.convertAndSend("cache:invalidate", "states");
            redisTemplate.convertAndSend("cache:invalidate", "state");
            redisTemplate.convertAndSend("cache:invalidate", "statesLastUpdate");
            redisTemplate.convertAndSend("cache:invalidate", "statesLastUpdateById");

            stateRepository.delete(findById(id));
            stateRepository.flush();


        } catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(id);
        }
    }
}
