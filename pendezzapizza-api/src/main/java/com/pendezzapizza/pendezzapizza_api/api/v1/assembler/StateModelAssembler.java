package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.StateMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.StateModel;
import com.pendezzapizza.pendezzapizza_api.core.security.PendezzaPizzaSecurity;
import com.pendezzapizza.pendezzapizza_api.domain.model.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class StateModelAssembler{

    @Autowired
    private StateMapper stateMapper;

    @Autowired
    private PendezzaPizzaSecurity pendezzaPizzaSecurity;

    public StateModel toModel(State state) {
        return stateMapper.toModel(state);
    }

    public Collection<StateModel> toCollectionModel(Collection<State> entities) {
        return entities.stream().map((this::toModel)).toList();
    }
}