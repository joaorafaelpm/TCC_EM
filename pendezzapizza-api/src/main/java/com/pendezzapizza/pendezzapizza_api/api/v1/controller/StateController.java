package com.pendezzapizza.pendezzapizza_api.api.v1.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.StateModelAssembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.disassambler.StateDisassembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.StateModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.StateDTO;
import com.pendezzapizza.pendezzapizza_api.api.v1.openapi.controller.StateControllerOpenApi;
import com.pendezzapizza.pendezzapizza_api.core.security.CheckSecurity;
import com.pendezzapizza.pendezzapizza_api.domain.model.State;
import com.pendezzapizza.pendezzapizza_api.domain.repository.StateRepository;
import com.pendezzapizza.pendezzapizza_api.domain.service.StateService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@RestController
@ResponseBody
@AllArgsConstructor
@RequestMapping(value = "/v1/states" , produces = MediaType.APPLICATION_JSON_VALUE)
public class StateController implements StateControllerOpenApi {

    private final StateRepository stateRepository;
    private final StateService stateService;
    private final StateModelAssembler stateModelAssembler;
    private final StateDisassembler stateDisassembler;

    @CheckSecurity.States.CanConsult
    @GetMapping
    public Collection<StateModel> all() {
        return stateModelAssembler.toCollectionModel(stateRepository.findAll());
    }

    @CheckSecurity.States.CanConsult
    @GetMapping("/{stateId}")
    public StateModel findById(@PathVariable UUID stateId) {
        return stateModelAssembler.toModel(stateService.findById(stateId));
    }

    @CheckSecurity.States.CanEdit
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StateModel add(@RequestBody @Valid StateDTO stateDTO) {
        State state = stateDisassembler.stateDTOToState(stateDTO);
        return stateModelAssembler.toModel(stateService.save(state));
    }

    @CheckSecurity.States.CanEdit
    @PutMapping("/{stateId}")
    public StateModel save(@PathVariable UUID stateId, @RequestBody @Valid StateDTO stateDTO) {
        State existingState = stateService.findById(stateId);
        stateDisassembler.updateStateFromDto(stateDTO, existingState);
        return stateModelAssembler.toModel(stateService.save(existingState));
    }

    @CheckSecurity.States.CanEdit
    @DeleteMapping("/{stateId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> remove(@PathVariable UUID stateId) {
        stateService.delete(stateId);
        return ResponseEntity.noContent().build();
    }
}