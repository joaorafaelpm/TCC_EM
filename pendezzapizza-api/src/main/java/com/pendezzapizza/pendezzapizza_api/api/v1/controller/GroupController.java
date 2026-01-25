package com.pendezzapizza.pendezzapizza_api.api.v1.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.GroupModelAssembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.disassambler.GroupDisassembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.GroupModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.GroupDTO;
import com.pendezzapizza.pendezzapizza_api.api.v1.openapi.controller.GroupControllerOpenApi;
import com.pendezzapizza.pendezzapizza_api.core.security.CheckSecurity;
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
public class GroupController implements GroupControllerOpenApi {

    private final GroupService groupService;
    private final GroupModelAssembler groupAssembler;
    private final GroupDisassembler groupDisassembler;

    @CheckSecurity.UsersGroupsPermissions.CanConsult
    @GetMapping
    public CollectionModel<GroupModel> all() {
        return groupAssembler.toCollectionModel(groupService.findAll());
    }

    @CheckSecurity.UsersGroupsPermissions.CanConsult
    @GetMapping("/{groupId}")
    public GroupModel findById(@PathVariable UUID groupId) {
        return groupAssembler.toModel(groupService.findById(groupId));
    }

    @CheckSecurity.UsersGroupsPermissions.CanEdit
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GroupModel add(@RequestBody @Valid GroupDTO groupDTO) {
        Group group = groupDisassembler.groupDTOToGroup(groupDTO);
        return groupAssembler.toModel(groupService.save(group));
    }

    @CheckSecurity.UsersGroupsPermissions.CanEdit
    @PutMapping("/{groupId}")
    public GroupModel save(@PathVariable UUID groupId, @RequestBody @Valid GroupDTO groupDTO) {
        Group existingGroup = groupService.findById(groupId);
        groupDisassembler.updateGroupFromDto(groupDTO, existingGroup);

        return groupAssembler.toModel(groupService.save(existingGroup));
    }

    @CheckSecurity.UsersGroupsPermissions.CanEdit
    @DeleteMapping("/{groupId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> remove(@PathVariable UUID groupId) {
        groupService.deleteById(groupId);
        return ResponseEntity.noContent().build();
    }
}