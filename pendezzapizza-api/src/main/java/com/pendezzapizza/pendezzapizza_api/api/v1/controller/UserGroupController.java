package com.pendezzapizza.pendezzapizza_api.api.v1.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.GroupModelAssembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.GroupModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.openapi.controller.UserGroupControllerOpenApi;
import com.pendezzapizza.pendezzapizza_api.core.security.CheckSecurity;
import com.pendezzapizza.pendezzapizza_api.domain.model.Group;
import com.pendezzapizza.pendezzapizza_api.domain.service.GroupService;
import com.pendezzapizza.pendezzapizza_api.domain.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(path ="/v1/users/{userId}/groups", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class UserGroupController implements UserGroupControllerOpenApi {

    private final GroupService groupService;
    private final UserService userService;
    private final GroupModelAssembler groupAssembler;

    @CheckSecurity.UsersGroupsPermissions.CanConsult
    @GetMapping
    public ResponseEntity<Collection<GroupModel>> getAllGroupsFromUser(@PathVariable UUID userId , ServletWebRequest request) {
        ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());
        String eTag = "0";
        OffsetDateTime lastUpdateDate = userService.getLastUpdateDate();
        if (lastUpdateDate != null) {
            eTag = String.valueOf(lastUpdateDate.toEpochSecond());
        }

        if (request.checkNotModified(eTag)) {
            return null;
        }

        Set<Group> groups = groupService.findById(userId).getGroups();

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
                .eTag(eTag)
                .body(groupAssembler.toCollectionModel(groups));
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