package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.PendezzaPizzaLinks;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.StateMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.controller.StateController;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.StateModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StateModelAssembler extends RepresentationModelAssemblerSupport<State, StateModel> {

    @Autowired
    private StateMapper mapper;

    @Autowired
    private PendezzaPizzaLinks links;

    public StateModelAssembler() {
        super(StateController.class, StateModel.class);
    }

    @Override
    public StateModel toModel(State state) {
        StateModel model = mapper.toModel(state);

        model.add(links.linkToState(model.getId()));
        model.add(links.linkToStates());

        return model;
    }

    public CollectionModel<StateModel> toCollection(List<State> states) {
        var list = states.stream().map(this::toModel).toList();
        CollectionModel<StateModel> collection = CollectionModel.of(list);

        collection.add(links.linkToStates());

        return collection;
    }
}
