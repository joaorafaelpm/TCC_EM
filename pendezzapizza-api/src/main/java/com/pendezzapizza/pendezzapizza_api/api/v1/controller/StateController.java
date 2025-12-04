package com.pendezzapizza.pendezzapizza_api.api.v1.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.StateModelAssembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.disassambler.StateDisassembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.DTO.StateDTO;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.StateModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.State;
import com.pendezzapizza.pendezzapizza_api.domain.repository.StateRepository;
import com.pendezzapizza.pendezzapizza_api.domain.service.StateService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@ResponseBody
@AllArgsConstructor
@RequestMapping("/v1/states")
public class StateController {

    private StateRepository stateRepository;

    private StateService stateService;

    private StateModelAssembler stateModelAssembler;
    private StateDisassembler stateDisassembler;

    @GetMapping
    public CollectionModel<StateModel> getAll() {
        return stateModelAssembler.toCollection(stateRepository.findAll());
    }

    @GetMapping("/{id}")
    public StateModel getById(@PathVariable UUID id) {
        return stateModelAssembler.toModel(stateService.findById(id));
    }

    @PostMapping
    public StateModel add(@RequestBody @Valid StateDTO stateDTO) {
        State state = stateDisassembler.stateDTOToState(stateDTO);
        return stateModelAssembler.toModel(stateService.save(state));
    }

    @PutMapping("/{id}")
    public StateModel update(@PathVariable UUID id, @RequestBody @Valid StateDTO stateDTO) {
        State oldState = stateService.findById(id);
        stateDisassembler.updateStateFromDto(stateDTO, oldState);
        State savedState = stateService.save(id, oldState);
        return stateModelAssembler.toModel(savedState);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        stateService.delete(id);
    }
}
