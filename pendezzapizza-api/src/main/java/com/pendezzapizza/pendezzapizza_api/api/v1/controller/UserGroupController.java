package com.pendezzapizza.pendezzapizza_api.api.v1.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.GroupAssembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.GroupModel;
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
public class UserGroupController {

    private GroupService groupService;
    private UserService userService;

    private GroupAssembler groupAssembler;

    @GetMapping
    public CollectionModel<GroupModel> getAllGroupsFromUser(@PathVariable UUID userId) {
        return groupAssembler.toCollectionRefUser(
                userId,
                userService.findById(userId).getGroups()
        );
    }

    @PutMapping("/{groupId}")
    public ResponseEntity<Void> associate(@PathVariable UUID userId, @PathVariable UUID groupId) {
        groupService.associateGroup(userId, groupId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<Void> disassociate(@PathVariable UUID userId, @PathVariable UUID groupId) {
        groupService.disassociateGroup(userId, groupId);
        return ResponseEntity.noContent().build();
    }
}
