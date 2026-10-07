package com.pendezzapizza.pendezzapizza_api.api.v1.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.StateModelAssembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.disassembler.StateDisassembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.StateModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.StateDTO;
import com.pendezzapizza.pendezzapizza_api.api.v1.openapi.controller.StateControllerOpenApi;
import com.pendezzapizza.pendezzapizza_api.core.security.CheckSecurity;
import com.pendezzapizza.pendezzapizza_api.domain.model.State;
import com.pendezzapizza.pendezzapizza_api.domain.service.StateService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@ResponseBody
@AllArgsConstructor
@RequestMapping(value = "/v1/states" , produces = MediaType.APPLICATION_JSON_VALUE)
public class StateController implements StateControllerOpenApi {

    private final StateService stateService;
    private final StateModelAssembler stateModelAssembler;
    private final StateDisassembler stateDisassembler;

    @CheckSecurity.States.CanConsult
    @GetMapping
    public ResponseEntity<Page<StateModel>> all(@RequestParam(required = false)String stateName,  Pageable pageable , ServletWebRequest request) {
        Page<State> states;
        ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());
        String eTag = "0";
        OffsetDateTime lastUpdateDate = stateService.getLastUpdateDate();
        if (lastUpdateDate != null) {
            eTag = String.valueOf(lastUpdateDate.toEpochSecond());
        }

        if (request.checkNotModified(eTag)) {
            return null;
        }

        if (stateName == null) {
            states = stateService.findAll(pageable);
        }
        else {
            states = stateService.findByName(stateName,pageable);
        }

        Page<StateModel> statesModel = states.map(stateModelAssembler::toModel);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
                .eTag(eTag)
                .body(statesModel);

    }

    @CheckSecurity.States.CanConsult
    @GetMapping("/{stateId}")
    public ResponseEntity<StateModel> findById(@PathVariable UUID stateId, ServletWebRequest request) {
        ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());
        String eTag = "0";
        OffsetDateTime lastUpdateDate = stateService.getLastUpdateDate();
        if (lastUpdateDate != null) {
            eTag = String.valueOf(lastUpdateDate.toEpochSecond());
        }

        if (request.checkNotModified(eTag)) {
            return null;
        }

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
                .eTag(eTag)
                .body(stateModelAssembler.toModel(stateService.findById(stateId)));
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