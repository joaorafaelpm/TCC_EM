package com.pendezzapizza.pendezzapizza_api.api.v1.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.PermissionModelAssembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.PermissionModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.openapi.controller.GroupPermissionControllerOpenApi;
import com.pendezzapizza.pendezzapizza_api.core.security.CheckSecurity;
import com.pendezzapizza.pendezzapizza_api.domain.service.GroupService;
import lombok.AllArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(path = "/v1/groups/{groupId}/permissions", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class GroupPermissionController implements GroupPermissionControllerOpenApi {

    private final GroupService groupService;
    private final PermissionModelAssembler permissionAssembler;

    @CheckSecurity.UsersGroupsPermissions.CanConsult
    @GetMapping
    public ResponseEntity<List<PermissionModel>> listPermissions(@PathVariable UUID groupId , ServletWebRequest request) {
        ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());
        String eTag = "0";
        OffsetDateTime lastUpdateDate = groupService.getLastUpdateDateById(groupId);

        if (lastUpdateDate != null) {
            eTag = String.valueOf(lastUpdateDate.toEpochSecond());
        }

        if (request.checkNotModified(eTag)) {
            return null;
        }
        List<PermissionModel> permissions = permissionAssembler.toCollectionModel(groupService.findById(groupId).getPermission()).stream().toList();

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
                .eTag(eTag)
                .body(permissions);
    }

    @CheckSecurity.UsersGroupsPermissions.CanEdit
    @PutMapping("/{permissionId}")
    public ResponseEntity<Void> associatePermission(@PathVariable UUID groupId, @PathVariable UUID permissionId) {
        groupService.associatePermission(groupId, permissionId);
        return ResponseEntity.noContent().build();
    }

    @CheckSecurity.UsersGroupsPermissions.CanEdit
    @DeleteMapping("/{permissionId}")
    public ResponseEntity<Void> disassociatePermission(@PathVariable UUID groupId, @PathVariable UUID permissionId) {
        groupService.disassociatePermission(groupId, permissionId);
        return ResponseEntity.noContent().build();
    }
}