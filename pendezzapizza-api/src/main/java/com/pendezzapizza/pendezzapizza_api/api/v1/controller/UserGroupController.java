package com.pendezzapizza.pendezzapizza_api.api.v1.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.GroupModelAssembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.GroupModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.openapi.controller.UserGroupControllerOpenApi;
import com.pendezzapizza.pendezzapizza_api.core.security.CheckSecurity;
import com.pendezzapizza.pendezzapizza_api.domain.service.GroupService;
import com.pendezzapizza.pendezzapizza_api.domain.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/users/{userId}/groups")
@AllArgsConstructor
public class UserGroupController implements UserGroupControllerOpenApi {

    private final GroupService groupService;
    private final UserService userService;
    private final GroupModelAssembler groupAssembler;

    @CheckSecurity.UsersGroupsPermissions.CanConsult
    @GetMapping
    public CollectionModel<GroupModel> getAllGroupsFromUser(@PathVariable UUID userId) {
        return groupAssembler.toCollectionRefUser(userId, userService.findById(userId).getGroups());
    }

    @CheckSecurity.UsersGroupsPermissions.CanEdit
    @PutMapping("/{groupId}")
    public ResponseEntity<Void> associate(@PathVariable UUID userId, @PathVariable UUID groupId) {
        groupService.associateGroup(userId, groupId);
        return ResponseEntity.noContent().build();
    }

    @CheckSecurity.UsersGroupsPermissions.CanEdit
    @DeleteMapping("/{groupId}")
    public ResponseEntity<Void> disassociate(@PathVariable UUID userId, @PathVariable UUID groupId) {
        groupService.disassociateGroup(userId, groupId);
        return ResponseEntity.noContent().build();
    }
}