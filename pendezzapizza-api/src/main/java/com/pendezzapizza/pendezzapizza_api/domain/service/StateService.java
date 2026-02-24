package com.pendezzapizza.pendezzapizza_api.domain.service;

import com.pendezzapizza.pendezzapizza_api.domain.exception.EntityInUseException;
import com.pendezzapizza.pendezzapizza_api.domain.model.State;
import com.pendezzapizza.pendezzapizza_api.domain.repository.StateRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class StateService {

    private final StateRepository stateRepository;

    public State findById(UUID id) {
        return stateRepository.findByIdOrThrowException(id);
    }

    @Transactional
    public State save(State state) {
        return stateRepository.save(state);
    }

    @Transactional
    public void delete(UUID id) {
        try {
            stateRepository.delete(findById(id));
            stateRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(id);
        }
    }
}
