package com.pendezzapizza.pendezzapizza_api.api.v1.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.GroupAssembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.disassambler.GroupDisassembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.DTO.GroupDTO;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.GroupModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.Group;
import com.pendezzapizza.pendezzapizza_api.domain.service.GroupService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/groups")
@AllArgsConstructor
public class GroupController {

    private GroupService groupService;

    private GroupAssembler groupAssembler;
    private GroupDisassembler groupDisassembler;

    @GetMapping
    public CollectionModel<GroupModel> findAll() {
        return groupAssembler.toCollection(groupService.findAll());
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<GroupModel> findById(@PathVariable UUID groupId) {
        return ResponseEntity.ok(
                groupAssembler.toModel(groupService.findById(groupId))
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GroupModel save(@RequestBody @Valid GroupDTO groupDTO) {
        Group group = groupDisassembler.groupDTOToGroup(groupDTO);
        return groupAssembler.toModel(groupService.save(group));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupModel> update(
            @PathVariable UUID id,
            @RequestBody @Valid GroupDTO groupDTO
    ) {
        Group existingGroup = groupService.findById(id);
        groupDisassembler.updateGroupFromDto(groupDTO, existingGroup);

        return ResponseEntity.ok(
                groupAssembler.toModel(groupService.save(existingGroup))
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        groupService.deleteById(id);
    }
}
