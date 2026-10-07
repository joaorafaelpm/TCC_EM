package com.pendezzapizza.pendezzapizza_api.api.v1.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.GroupModelAssembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.disassembler.GroupDisassembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.GroupModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.GroupDTO;
import com.pendezzapizza.pendezzapizza_api.api.v1.openapi.controller.GroupControllerOpenApi;
import com.pendezzapizza.pendezzapizza_api.core.security.CheckSecurity;
import com.pendezzapizza.pendezzapizza_api.domain.model.Group;
import com.pendezzapizza.pendezzapizza_api.domain.service.GroupService;
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
@RequestMapping(path = "/v1/groups", produces = MediaType.APPLICATION_JSON_VALUE)

@AllArgsConstructor
public class GroupController implements GroupControllerOpenApi {

    private final GroupService groupService;
    private final GroupModelAssembler groupAssembler;
    private final GroupDisassembler groupDisassembler;

    @CheckSecurity.UsersGroupsPermissions.CanConsult
    @GetMapping
    public ResponseEntity<Page<GroupModel>> all(@RequestParam(required = false) String groupName ,Pageable pageable, ServletWebRequest request) {
        Page<Group> groups;
        ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());
        String eTag = "0";
        OffsetDateTime lastUpdateDate = groupService.getLastUpdateDate();
        if (lastUpdateDate != null) {
            eTag = String.valueOf(lastUpdateDate.toEpochSecond());
        }

        if (request.checkNotModified(eTag)) {
            return null;
        }

        if (groupName != null) {
            groups = groupService.findAllByName(groupName , pageable);
        }
        else {
            groups = groupService.findAll(pageable);
        }

        Page<GroupModel> groupModels = groups.map(groupAssembler::toModel);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
                .eTag(eTag)
                .body(groupModels);

    }

    @CheckSecurity.UsersGroupsPermissions.CanConsult
    @GetMapping("/{groupId}")
    public ResponseEntity<GroupModel> findById(@PathVariable UUID groupId, ServletWebRequest request) {
        ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());
        String eTag = "0";
        OffsetDateTime lastUpdateDate = groupService.getLastUpdateDateById(groupId);
        if (lastUpdateDate != null) {
            eTag = String.valueOf(lastUpdateDate.toEpochSecond());
        }

        if (request.checkNotModified(eTag)) {
            return null;
        }

        GroupModel model = groupAssembler.toModel(groupService.findById(groupId));
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
                .eTag(eTag)
                .body(model);
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