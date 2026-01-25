package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.PendezzaLinks;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.StateMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.controller.StateController;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.StateModel;
import com.pendezzapizza.pendezzapizza_api.core.security.PendezzaPizzaSecurity;
import com.pendezzapizza.pendezzapizza_api.domain.model.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class StateModelAssembler extends RepresentationModelAssemblerSupport<State, StateModel> {

    @Autowired
    private StateMapper stateMapper;

    @Autowired
    private PendezzaLinks pendezzaPizzaLinks;

    @Autowired
    private PendezzaPizzaSecurity pendezzaPizzaSecurity;

    public StateModelAssembler() {
        super(StateController.class, StateModel.class);
    }

    @Override
    public StateModel toModel(State state) {
        StateModel stateModel = stateMapper.toModel(state);

        if (pendezzaPizzaSecurity.canConsultStates()) {
            stateModel.add(pendezzaPizzaLinks.linkToState(stateModel.getId()));
            stateModel.add(pendezzaPizzaLinks.linkToStates());
        }

        return stateModel;
    }

    @Override
    public CollectionModel<StateModel> toCollectionModel(Iterable<? extends State> entities) {
        CollectionModel<StateModel> statesCollectionModel = super.toCollectionModel(entities);

        if (pendezzaPizzaSecurity.canConsultStates()) {
            statesCollectionModel.add(pendezzaPizzaLinks.linkToStates());
        }

        return statesCollectionModel;
    }
}