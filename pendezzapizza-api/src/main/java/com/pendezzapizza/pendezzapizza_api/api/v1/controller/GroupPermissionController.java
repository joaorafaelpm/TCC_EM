package com.pendezzapizza.pendezzapizza_api.api.v1.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.PermissionAssembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.PermissionModel;
import com.pendezzapizza.pendezzapizza_api.domain.service.GroupService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/groups/{groupId}/permissions")
@AllArgsConstructor
public class GroupPermissionController {

    private GroupService groupService;

    private PermissionAssembler permissionAssembler;

    @GetMapping
    public CollectionModel<PermissionModel> listPermissions(@PathVariable UUID groupId) {
        return permissionAssembler.toCollectionRefGroup(
                groupId,
                groupService.findById(groupId).getPermission()
        );
    }

    @PutMapping("/{permissionId}")
    public ResponseEntity<Void> associatePermission(
            @PathVariable UUID groupId,
            @PathVariable UUID permissionId
    ) {
        groupService.associate(groupId, permissionId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{permissionId}")
    public ResponseEntity<Void> disassociatePermission(
            @PathVariable UUID groupId,
            @PathVariable UUID permissionId
    ) {
        groupService.disassociate(groupId, permissionId);
        return ResponseEntity.noContent().build();
    }
}
